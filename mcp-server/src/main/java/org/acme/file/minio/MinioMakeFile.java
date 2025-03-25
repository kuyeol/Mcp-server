package org.acme.file.minio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.minio.MinioAsyncClient;
import io.minio.PutObjectArgs;
import org.acme.agent.Agentable;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.Map;

public class MinioMakeFile implements Agentable
{


  private final MinioAsyncClient asyncClient;
  private final Gson             gson = new Gson();

  public MinioMakeFile(MinioAsyncClient asyncClient)
  {
    this.asyncClient = asyncClient;

  }

  public String[] value() {
    return new String[0];
  }


  @Override
  public String toolName() {
    return "MakeFile";
  }

  @Override
  public String executeCommand(Object input) {
    Type                mapType    = new TypeToken<Map<String, String>>()
    {
    }.getType();
    Map<String, String> parameters = gson.fromJson((String) input, mapType);

    String bucketName = parameters.get("bucketName");
    String fileName   = parameters.get("fileName");
    String content    = parameters.get("content");

    if (bucketName == null || fileName == null || content == null) {
      return "오류: bucketName, fileName, content는 필수 파라미터입니다.";
    }

    try {
      byte[] contentBytes = content.getBytes();
      try (ByteArrayInputStream stream = new ByteArrayInputStream(contentBytes)) {
        asyncClient.putObject(
          PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(stream, contentBytes.length, -1).build());
      }
      return "파일 생성 성공: " + bucketName + "/" + fileName;
    } catch (Exception e) {
      return "파일 생성 실패: " + e.getMessage();
    }
  }


@Override
public String description() {
  return "{\n" + "  \"name\": \"create_file\",\n" + "  \"description\": \"MinIO에 새 파일을 생성합니다.\",\n" + "  \"parameters\": {\n" + "    \"type\": \"object\",\n" + "    \"properties\": {\n" + "      \"bucketName\": {\n" + "        \"type\": \"string\",\n" + "        \"description\": \"MinIO 버킷 이름\"\n" + "      },\n" + "      \"fileName\": {\n" + "        \"type\": \"string\",\n" + "        \"description\": \"파일 이름\"\n" + "      },\n" + "      \"content\": {\n" + "        \"type\": \"string\",\n" + "        \"description\": \"파일 내용\"\n" + "      }\n" + "    },\n" + "    \"required\": [\n" + "      \"bucketName\",\n" + "      \"fileName\",\n" + "      \"content\"\n" + "    ]\n" + "  }\n" + "}";

}
}
