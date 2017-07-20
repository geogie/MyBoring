package com.georgeren.myboring.utils;

/**
 * Created by georgeRen on 2017/7/20.
 */

public class StringUtils {
    public static String getCSSStyle(String tempCssString) {
        StringBuilder builder = new StringBuilder();
        builder.append("<style type=\"text/css\">");
        builder.append(tempCssString);
        builder.append("</style> ");
        return builder.toString();
    }

    public static String getHTMLHead(String... links) {
        StringBuilder builder = new StringBuilder();
        builder.append("<head>");
        for (String link : links) {
            builder.append(link);
        }
        builder.append("<meta name=\\\"viewport\\\" content=\\\"width=device-width, user-scalable=yes\\\" />");
        builder.append("</head>");
        return builder.toString();
    }

    public static String getHtmlString(String head, String body) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append(head);
        builder.append("<body>");
        builder.append(body);
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }

    public static String adjustEsssayHtmlStyle(String htmlString) {
        return htmlString.replace("class=\"img-place-holder\"", "");
    }
}
