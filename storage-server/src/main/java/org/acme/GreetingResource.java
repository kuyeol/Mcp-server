package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.file.SourceUpload;

@Path("/hello")
@ApplicationScoped
public class GreetingResource
{

    @Inject
    SourceUpload sourceUpload;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        //동일 요청 반복 시 예외 처리 로직
        // 덮어쓰기 , 시간 등...
        sourceUpload.setBucket("aidata", "object");
        sourceUpload.uploadFile("qsssurssssskus", "fil1e.txt");

        return "Hello from Quarkus REST";
    }




}
