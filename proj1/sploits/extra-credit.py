#!/usr/bin/python2
import sys
import socket
import traceback
import struct

####

## This function takes your exploit code, adds a carriage-return and newline
## and sends it to the server. The server will always respond, but if the
## exploit crashed the server it will close the connection. Therefore, we try
## to write another query to the server, recv on the socket and see if we get
## an exception
##
## True means the exploit made the server close the connection (i.e. it crashed)
## False means the socket is still operational.
def try_exploit(exploit, sock):
    sock.send("%s\r\n" % exploit)
    sock.recv(len(exploit) + 2) # the exploit will always be returned
    try:
        sock.send("hello\r\n")
        sock.recv(7)
        return False
    except:
        return True

def exploit(host, port, shellcode):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("Connecting to %s:%d..." % (host, port))
    sock.connect((host, port))

    print("Connected, sending test request \"Hello\"...")
    sock.send("Hello\r\n")
    resp = sock.recv(7)
    print("Response was \"%s\"!" % resp.strip())

    print("OK, ready to h/\X0r...")

    # Build your exploit here
    # One useful function might be
    #   struct.pack("<I", x)
    # which returns the 4-byte binary encoding of the 32-bit integer x
    while True:
        if try_exploit("my_exploit_here", sock):
            # socket was closed by server (e.g. process died)
            break
        else:
            # socket still open
            continue

    sock.close()

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

