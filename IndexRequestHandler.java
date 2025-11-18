import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class IndexRequestHandler implements HttpRequestHandler {

  private String getDeletePostForm(Post post, String currentUsername) {
    if (post.getUsername().equals(currentUsername)) {
      return """
          <form action="/posts/delete" method="POST" class="flex items-center justify-center">
            <input type="hidden" name="postId" value="{{postId}}" />
            <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600">
              <i class="fa-solid fa-trash"></ i>
            </button>
          </form>""".replace("{{postId}}", post.getId().toString());
    }
    return "";
  }

  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();
    String currentUsername = null;
    try {
      SessionManager sessionManager = new SessionManager();
      currentUsername = sessionManager.getUsername(session);

      PostRepository postRepository = new PostRepository();

      Post[] posts = postRepository.getAllPosts();

      for (int i = 0; i < posts.length / 2; i++) {
        Post temp = posts[i];
        posts[i] = posts[posts.length - 1 - i];
        posts[posts.length - 1 - i] = temp;
      }

      StringBuilder builder = new StringBuilder();

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy").withZone(ZoneId.of("Asia/Bishkek"));

      String postTemplate = """
                  <div class="post-item w-full max-w-2xl bg-white p-6 rounded-xl shadow-md mt-8 text-left">
                    <div class="flex items-center gap-2 text-sm text-gray-500 mb-2">
                      <i class="fa-regular fa-user"></i>
                      <span>{{username}}</span>
                    </div>

                    <h3 class="text-2xl font-semibold mb-2">{{title}}</h3>

                    <p class="text-gray-700 mb-4 whitespace-pre-line">{{content}}</p>

                    <div class=" flex items-center justify-between gap-2">
                    <div class="text-gray-400 text-sm">
                      <i class="fa-regular fa-clock"></i>
                      <span>{{createdAt}}</span>
          </div>
            {{deleteForm}}
                    </div>
                  </div>
                  """;

      for (Post post : posts) {
        String postHtml = postTemplate
            .replace("{{deleteForm}}", getDeletePostForm(post, currentUsername))
            .replace("{{postId}}", post.getId().toString())
            .replace("{{username}}", escape(post.getUsername()))
            .replace("{{title}}", escape(post.getTitle()))
            .replace("{{content}}", escape(post.getContent()))
            .replace("{{createdAt}}", formatter.format(post.getCreatedAt()));
        builder.append(postHtml);
      }
      String body = HtmlRenderer.renderWithVariables("index.html", Map.of("posts", builder.toString()));
      return HttpResponseFactory.createOkResponse(body);
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }

  private String escape(String s) {
    return s
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;");
  }
}
