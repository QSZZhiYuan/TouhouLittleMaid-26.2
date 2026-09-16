package com.github.tartaricacid.touhoulittlemaid.util;

public final class ShapeDraw {
    public static void drawSector(int x, int y, int r, double startAngle, double endAngle, int precision, int color) {
        // TODO: 26.2 渲染系统重构，Tesselator/BufferUploader/VertexFormat.Mode 已移除，形状绘制暂时禁用
    }

    public static void drawCircle(int x, int y, int r, int precision, int color) {
        drawSector(x, y, r, 0, 2 * Math.PI, precision, color);
    }
}
