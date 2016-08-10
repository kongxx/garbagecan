
struct Job {
    1: string id,
    2: string name,
    3: string queue,
    4: string user,
    5: string cmd,
    6: string json
}

exception JobServiceException {
  1: i32 code,
  2: string message
}

service JobService {
    string open() throws (1: JobServiceException e),
    void close(1: string operationId) throws (1: JobServiceException e),
    list<Job> getJobs(1: string operationId, 2: i32 offset, 3: i32 size) throws (1: JobServiceException e)
}
