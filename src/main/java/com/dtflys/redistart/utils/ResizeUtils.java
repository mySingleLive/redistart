package com.dtflys.redistart.utils;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeUtils {

    //窗体拉伸属性
    private static boolean isLeft;// 是否处于左边界调整窗口状态
    private static boolean isRight;// 是否处于右边界调整窗口状态
    private static boolean isBottomLeft;// 是否处于左下角调整窗口状态
    private static boolean isBottomRight;// 是否处于右下角调整窗口状态
    private static boolean isBottom;// 是否处于下边界调整窗口状态
    private static boolean isTop;// 是否处于上边界调整窗口状态
    private static boolean isTopLeft;// 是否处于左上边界调整窗口状态
    private static boolean isTopRight;// 是否处于右上边界调整窗口状态
    private final static int RESIZE_WIDTH = 5;// 判定是否为调整窗口状态的范围与边界距离
    private final static double MIN_WIDTH = 300;// 窗口最小宽度
    private final static double MIN_HEIGHT = 250;// 窗口最小高度
    private static double offsetX = 0;
    private static double offsetY = 0;

    public static void addResizeListener(Stage stage, Parent root) {

        root.setOnMouseMoved((MouseEvent event) -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
            isLeft = isRight = isBottomLeft = isBottomRight = isBottom = false;
            if (y >= height - RESIZE_WIDTH) {
                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
                    isBottomLeft = true;
                    cursorType = Cursor.SW_RESIZE;
                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
                    isBottomRight = true;
                    cursorType = Cursor.SE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            }  else if (y <= RESIZE_WIDTH) {// 上边界调整窗口状态
                if (x <= RESIZE_WIDTH) {// 左上角调整窗口状态
                    isTopLeft = true;
                    cursorType = Cursor.NW_RESIZE;
                } else if (x >= width - RESIZE_WIDTH) {// 右上角调整窗口状态
                    isTopRight = true;
                    cursorType = Cursor.NE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            } else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            } else if (x <= RESIZE_WIDTH) {
                isLeft = true;
                cursorType = Cursor.W_RESIZE;
            }
            // 最后改变鼠标光标
            root.setCursor(cursorType);
        });

        root.setOnMousePressed(event -> {
            if (isLeft || isTopLeft || isBottomLeft || isTop || isTopRight) {
                offsetX = event.getSceneX();
                offsetY = event.getSceneY();
            }
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double screenX = event.getScreenX();
            double screenY = event.getScreenY();
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = stage.getX();
            double nextY = stage.getY();
            double nextWidth = stage.getWidth();
            double nextHeight = stage.getHeight();
            if (isRight || isBottomRight || isTopRight) {// 所有右边调整窗口状态
                nextWidth = x;
            }
            if (isLeft || isBottomLeft || isTopLeft) {// 所有左边调整窗口状态
//                if (x > 0) {
//                    nextX = x;
//                }
                double oldRightX = nextX + nextWidth;
                nextX = screenX - offsetX;
                nextWidth = oldRightX - nextX;
//                nextWidth = nextWidth - (nextX - x);
            }
            if (isBottomRight || isBottomLeft || isBottom) {// 所有下边调整窗口状态
                nextHeight = y;
            }
            if (isTopRight || isTopLeft || isTop) {// 所有上边调整窗口状态
                double oldBottomY = nextY + nextHeight;
                nextY = screenY - offsetY;
                nextHeight = oldBottomY - nextY;
            }
            if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                nextWidth = MIN_WIDTH;
            }
            if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                nextHeight = MIN_HEIGHT;
            }
            // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
            stage.setX(nextX);
            stage.setY(nextY);
            stage.setWidth(nextWidth);
            stage.setHeight(nextHeight);
        });
    }

}
