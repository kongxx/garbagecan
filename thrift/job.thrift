
struct Job {
    1: string id,
    2: string name,
    3: string queue,
    4: string user,
    5: string cmd,
}

service JobService {
    list<Job> getJobs(1: i32 size)
}
