package moe.langua.lab.utils.logger.utils;

public class Utils {
    public static String color(String content, Color color, Color colorAppend) {
        return color.colorPrefix() + content + colorAppend.colorPrefix();
    }

    public enum Color {
        WHITE(30), RED(31), GREEN(32), YELLOW(33), BLUE(34), PURPLE(35), LIGHT_BLUE(36), GRAY(37);
        int value;

        Color(int value) {
            this.value = value;
        }

        String colorPrefix() {
            return "\033[" + value + "m";
        }
    }
}
