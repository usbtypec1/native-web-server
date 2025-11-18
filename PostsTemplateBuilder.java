import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PostsTemplateBuilder {
  private static final DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("HH:mm dd.MM.yyyy")
      .withZone(ZoneId.of("Asia/Bishkek"));
  private static final String POST_TEMPLATE = """
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
      </div>""";
  private static final String DELETE_POST_FORM_TEMPLATE = """
      <form action="/posts/delete" method="POST" class="flex items-center justify-center">
        <input type="hidden" name="postId" value="{{postId}}" />
          <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600">
            Delete
          </button>
      </form>""";

  private StringBuilder builder;

  public PostsTemplateBuilder() {
    this.builder = new StringBuilder();
  }

  public String toString() {
    return builder.toString();
  }

  public void appendPost(Post post, String currentUsername) {
    String postHtml = POST_TEMPLATE
        .replace("{{deleteForm}}", getDeletePostFormHtml(post, currentUsername))
        .replace("{{postId}}", post.getId().toString())
        .replace("{{username}}", HtmlUtils.escape(post.getUsername()))
        .replace("{{title}}", HtmlUtils.escape(post.getTitle()))
        .replace("{{content}}", HtmlUtils.escape(post.getContent()))
        .replace("{{createdAt}}", formatter.format(post.getCreatedAt()));
    builder.append(postHtml);
  }

  private String getDeletePostFormHtml(Post post, String currentUsername) {
    if (!post.getUsername().equals(currentUsername)) {
      return "";
    }
    return DELETE_POST_FORM_TEMPLATE.replace("{{postId}}", post.getId().toString());
  }
}