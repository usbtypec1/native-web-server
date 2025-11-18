public class HeaderTemplate {
  public static String GUEST_USER_HEADER = """
      <header class="p-4 shadow-md bg-white flex justify-between items-center">
        <h1 class="text-xl font-bold"><a href="/"><i class="fa-solid fa-comment"></i> Postland</a></h1>
        <nav class="space-x-4 text-sm">
          <a href="/about" class="hover:text-blue-500">
            <i class="fa-solid fa-info-circle"></i>
            <span>About</span>
          </a>
          <a href="/login" class="hover:text-blue-500">Login</a>
          <a href="/register" class="hover:text-blue-500">Register</a>
        </nav>
      </header>""";
  public static String LOGGED_IN_USER_HEADER = """
      <header class="p-4 shadow-md bg-white flex justify-between items-center">
        <h1 class="text-xl font-bold"><a href="/"><i class="fa-solid fa-comment"></i> Postland</a></h1>
          <nav class="space-x-4 text-sm">
          <a href="/about" class="hover:text-blue-500">
            <i class="fa-solid fa-info-circle"></i>
            <span>About</span>
          </a>
          <a href="/logout" class="hover:text-blue-500">
            <i class="fa-solid fa-right-from-bracket"></i>
            <span>Logout</span>
          </a>
        </nav>
      </header>""";

  public static String getTemplate(boolean isLoggedIn) {
    return isLoggedIn ? LOGGED_IN_USER_HEADER : GUEST_USER_HEADER;
  }
}
