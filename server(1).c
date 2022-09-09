#include "common.h" // This is needed for sleep()

void serviceClient(pid_t);

int main(int argc, char *argv[]){
  int fd;
  char ch, serverFifoName[25];
  pid_t clientPid;

  if(argc != 2){
    printf("Usage: %s <listening FIFO Name>\n", argv[0]);
    exit(0);
  }
  sprintf(serverFifoName,"/tmp/%s",argv[1]);// create fifo name
  unlink(serverFifoName);                   // delete if it exists
  if(mkfifo(serverFifoName, 0777)!=0)
    exit(1);
  chmod(serverFifoName, 0777);

  while(1){
    fprintf(stderr, "Main server: waiting for a new client\n");
    fd = open(serverFifoName, O_RDONLY);
    read(fd,&clientPid, sizeof(pid_t));  // get client's pid
    fprintf(stderr, "Main server: got a client: %d\n", clientPid);

    if (!fork()){
      close(fd);  // main fifo not needed by child
      serviceClient(clientPid); // child will service client
    }
  }
  exit(0);
}
void serviceClient(pid_t clientPid){
  char ch[255], prompt[10], *bye="Bye Bye, client disconnected\n";
  char name[25];
  int fd, n;

  sprintf(prompt, "%d: ", clientPid); // create a prompt
  sprintf(name, "/tmp/%d", clientPid);// new private fifo 
  if(mkfifo(name, 0777)!=0)
    exit(1);
  chmod(name, 0777);
  fd = open(name, O_RDONLY);
  while((n=read(fd, ch, 255)) >0){
    write(1, prompt, strlen(prompt));
    write(1, ch, n);
  }
  write(1, prompt, strlen(prompt));
  write(1, bye, strlen(bye));
  close(fd);
  exit(0);
}
