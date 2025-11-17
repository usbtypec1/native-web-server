public class SimpleHtmlPageHandler implements HttpRequestHandler {
  private String templateName;

  public SimpleHtmlPageHandler(String templateName) {
    this.templateName = templateName;
  }

  public HttpResponse getResponse(HttpRequest request) {
    return new HtmlTemplateResponse(HtmlRenderer.readTemplateFromFile(templateName));
  }
}
