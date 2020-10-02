#include<iostream>
#include<cstdio>
#include<pthread.h>
#include<unistd.h>
#include<semaphore.h>
#include<cstring>
#include <time.h>
#include <vector>

using namespace std;

#define number_of_servicemen 3
#define capacity_of_payment_room 2
#define number_of_cycles 10

sem_t entry[number_of_servicemen], payment;//entry for rooms,exting if anyone is leaving,making way//for someone to exit
pthread_mutex_t ext, dummy;

int leaving_count = 0;

void* task(void* arg){
    int ran;
    //lock dummy lock
    pthread_mutex_lock(&dummy);
    while(true)
    {
        //lock dummy lock
        pthread_mutex_lock(&ext);
        if(leaving_count == 0){
            sem_wait(&entry[0]);
            printf("%s started taking service from serviceman 1\n",(char *)arg);
            fflush(stdout);
            pthread_mutex_unlock(&ext);
            break;
        }
        pthread_mutex_unlock(&ext);
    
    }
    
    ran = ((rand()%300)+1)*5;
    usleep(ran);
    if(number_of_servicemen > 1){
        sem_wait(&entry[1]);
    }
    printf("%s finished taking service from serviceman 1\n",(char *)arg);
    fflush(stdout);
    sem_post(&entry[0]);    

    //unlock dummy lock
    pthread_mutex_unlock(&dummy);
    for(int i = 1 ; i < number_of_servicemen ; i++){
        printf("%s started taking service from serviceman %d\n",(char *)arg,i+1);
        fflush(stdout);
        ran = ((rand()%300)+1)*5;
        usleep(ran);
        if(i < number_of_servicemen - 1){
            sem_wait(&entry[i+1]);
        }
        printf("%s finished taking service from serviceman %d\n",(char *)arg,i+1);
        fflush(stdout);
        sem_post(&entry[i]);
    }
    
    sem_wait(&payment);
        printf("%s started paying the service bill\n",(char *)arg);
        fflush(stdout);
        ran = ((rand()%400)+1)*5;
        usleep(ran);
        pthread_mutex_lock(&ext);
            leaving_count++;
            printf("%s finished paying the service bill\n",(char *)arg);
            fflush(stdout);
        pthread_mutex_unlock(&ext);
    sem_post(&payment);
    for(int i = 0 ; i < number_of_servicemen ; i++){
        sem_wait(&entry[i]);
    }
    ran = ((rand()%300)+1)*5;
    usleep(ran);
    for(int i = number_of_servicemen-1 ; i > -1 ; i--){
        sem_post(&entry[i]);
    }
    printf("%s has departed\n",(char *)arg);
    fflush(stdout);
    pthread_mutex_lock(&ext);
        leaving_count--;
    pthread_mutex_unlock(&ext);
    
    pthread_exit(NULL);
}




int main(){
    int res;
    res = sem_init(&payment, 0, capacity_of_payment_room);
    if(res != 0){
        printf("Failed\n");
    }


    for(int i  = 0 ; i < number_of_servicemen ; i++){
        res = sem_init(&entry[i], 0, 1);
        if(res != 0){
            printf("Failed\n");
        }
    }
    

    res = pthread_mutex_init(&ext,NULL);
    if(res != 0){
        printf("Failed\n");
    }


    res = pthread_mutex_init(&dummy,NULL);
    if(res != 0){
        printf("Failed\n");
    }


    pthread_t cyclists[number_of_cycles];
    for(int i = 0 ; i < number_of_cycles ; i++){
        char *id = new char[3];
        strcpy(id,to_string(i+1).c_str());
        res = pthread_create(&cyclists[i],NULL,task,(void *)id);
        if(res != 0){
            printf("Thread creation failed\n");
        }
    }
    
    for(int i = 0; i < number_of_cycles; i++){
        void *result;
        pthread_join(cyclists[i], &result);
        //printf("%s",(char*)result);
        //fflush(stdout);
    }

    res = sem_destroy(&payment);
    if(res != 0){
        printf("Failed\n");
    }
    
    for(int i  = 0 ; i < number_of_servicemen ; i++){
        res = sem_destroy(&entry[i]);
        if(res != 0){
            printf("Failed\n");
        }
    }
    

    res = pthread_mutex_destroy(&ext);
    if(res != 0){
        printf("Failed\n");
    }

    res = pthread_mutex_destroy(&dummy);
    if(res != 0){
        printf("Failed\n");
    }
        
}
