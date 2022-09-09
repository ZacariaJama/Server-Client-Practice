#include "common.h" // This is needed for sleep()


int main(int argc, char *argv[]){
  int fd, n;
  char ch[255], name[25], serverFifoName[25];
  pid_t myPid;

  if(argc != 2){
    printf("Usage: %s <listening FIFO Name>\n", argv[0]);
    exit(0);
  }
  sprintf(serverFifoName, "/tmp/%s", argv[1]);
  while((fd=open(serverFifoName, O_WRONLY))==-1){
    fprintf(stderr, "trying to connect\n");
    sleep(1);
  }
  printf("Connected: type in data to be sent\n");
  myPid=getpid();
  write(fd, &myPid, sizeof(pid_t));
  sprintf(name, "/tmp/%d", myPid);
  close(fd);
  
  while((fd=open(name, O_WRONLY))==-1){
    fprintf(stderr, "trying to connect\n");
    sleep(1);
  }	
  while((n=read(0, ch, 255))>0) // -1 is CTR-D
    write(fd, ch, n);
  close(fd);
  exit(0);
}
