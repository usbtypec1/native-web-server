import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class IndexRequestHandler implements HttpRequestHandler {
  public HttpResponse getResponse(HttpRequest request) {

    PostRepository postRepository = new PostRepository();
    UserRepository userRepository = new UserRepository();

    Post[] posts = postRepository.getPosts(10, 0);

    StringBuilder builder = new StringBuilder();

    Map<String, User> users = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy").withZone(ZoneId.of("Asia/Bishkek"));

    String postTemplate = """
        <div class="w-full max-w-2xl bg-white p-6 rounded-xl shadow-md mt-8 text-left">
          <div class="flex items-center gap-2 text-sm text-gray-500 mb-2">
            <i class="fa-regular fa-user"></i>
            <span>{username}</span>
          </div>

          <h3 class="text-2xl font-semibold mb-2">{title}</h3>

          <p class="text-gray-700 mb-4 whitespace-pre-line">{content}</p>

          <p class="text-gray-400 text-sm flex items-center gap-2">
            <i class="fa-regular fa-clock"></i> {createdAt}
          </p>
        </div>
        """;

    for (Post post : posts) {
      User user = users.get(post.getUsername());
      if (user == null) {
        try {
          user = userRepository.getUserByUsername(post.getUsername());
        } catch (UserNotFoundException e) {
        }
      }
      String username = user != null ? user.getUsername() : "Anonymous";

      builder.append(
          postTemplate
              .replace("{username}", escape(username))
              .replace("{title}", escape(post.getTitle()))
              .replace("{content}", escape(post.getContent()))
              .replace("{createdAt}", formatter.format(post.getCreatedAt())));
    }

    Map<String, String> variables = new HashMap<>();
    variables.put("posts", builder.toString());

    String body = HtmlRenderer.renderWithVariables("index.html", variables);

    return new HttpResponse(null, null, body);
  }

  private String escape(String s) {
    return s
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;");
  }
}
