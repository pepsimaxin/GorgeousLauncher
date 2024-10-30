package com.android.launcher3.folder;

import com.gorgeous.launcher3.util.LogUtils;

public class ClippedFolderIconLayoutRule {
    // 文件夹预览中最多/最少显示的应用数量
    public static final int MAX_NUM_ITEMS_IN_PREVIEW = 4;
    private static final int MIN_NUM_ITEMS_IN_PREVIEW = 2;

    // 文件夹预览中图标的最小和最大缩放比例
    private static final float MIN_SCALE = 0.44f;
    private static final float MAX_SCALE = 0.51f;
    // 调整图标在圆形布局中的半径增长比例
    private static final float MAX_RADIUS_DILATION = 0.25f;
    // The max amount of overlap the preview items can go outside of the background bounds.
    // 允许图标之间的重叠程度
    public static final float ICON_OVERLAP_FACTOR = 1 + (MAX_RADIUS_DILATION / 2f);
    private static final float ITEM_RADIUS_SCALE_FACTOR = 1.15f;

    public static final int EXIT_INDEX = -2;
    public static final int ENTER_INDEX = -3;

    private float[] mTmpPoint = new float[2];

    private float mAvailableSpace;    // 可用的空间大小，表示文件夹预览区域的宽高
    private float mRadius;            // 圆形排列的半径
    private float mIconSize;          // 图标大小
    private boolean mIsRtl;           // Right-To-Left
    private float mBaselineIconScale; // 图标缩放的基础比例

    /**
     * 初始化文件夹图标预览的布局规则
     * @param availableSpace 预览区域的可用空间（显示区域宽高），决定图标的排列范围
     * @param intrinsicIconSize 表示图标围绕文件夹图标排列的半径，依据 availableSpace 计算
     * @param rtl Right-To-Left
     */
    public void init(int availableSpace, float intrinsicIconSize, boolean rtl) {
        mAvailableSpace = availableSpace;
        mRadius = ITEM_RADIUS_SCALE_FACTOR * availableSpace / 2f;
        mIconSize = intrinsicIconSize;
        mIsRtl = rtl;
        mBaselineIconScale = availableSpace / (intrinsicIconSize * 1f);
    }

    /**
     * 根据图标的索引和当前显示的图标数量来计算图标的缩放比例和位置
     * @param index
     * @param curNumItems
     * @param params
     * @return
     */
    public PreviewItemDrawingParams computePreviewItemDrawingParams(int index, int curNumItems,
            PreviewItemDrawingParams params) {
//        LogUtils.d("@@@ Marco", "computePreviewItemDrawingParams -- index = " + index + ", curNumItems = " + curNumItems
//                    + ", params = " + params);
        // 根据当前文件夹中图标的数量，调用 scaleForItem 方法来获取每个图标的缩放比例，文件夹中的图标数量会影响图标的最终大小
        float totalScale = scaleForItem(curNumItems);
        float transX;  // 用来存储图标的平移位置
        float transY;  // 用来存储图标的平移位置

        // 如果当前的图标索引等于 EXIT_INDEX，表示图标即将退出预览状态，将图标放置在固定位置
        if (index == EXIT_INDEX) {
            LogUtils.d("@@@ Marco", "index -- 01");
            // 0 1 * <-- Exit position (row 0, col 2)
            // 2 3
            getGridPosition(0, 2, mTmpPoint);  // 计算图标的网格布局位置
        } else if (index == ENTER_INDEX) {    // 如果索引等于 ENTER_INDEX，表示图标正在进入预览状态
            LogUtils.d("@@@ Marco", "index -- 02");
            // 0 1
            // 2 3 * <-- Enter position (row 1, col 2)
            getGridPosition(1, 2, mTmpPoint);  // 计算该图标的位置
        } else if (index >= MAX_NUM_ITEMS_IN_PREVIEW) {  // 如果图标的索引大于最大预览数量（这里是 4），该图标将被置于文件夹中心（mAvailableSpace / 2 表示中心位置）
            LogUtils.d("@@@ Marco", "index -- 03");
            // Items beyond those displayed in the preview are animated to the center
            mTmpPoint[0] = mTmpPoint[1] = mAvailableSpace / 2 - (mIconSize * totalScale) / 2;
        } else {
            // 计算该索引的图标在当前图标总数中的准确位置
            LogUtils.d("@@@ Marco", "index = " + index + ", curNumItems = " + curNumItems
                    + ", mTmpPoint[0] = " + mTmpPoint[0] + ", mTmpPoint[1] = " + mTmpPoint[1]);
            getPosition(index, curNumItems, mTmpPoint);
        }

        // 将 mTmpPoint 计算出来的平移位置存入 transX 和 transY 中
        transX = mTmpPoint[0];
        transY = mTmpPoint[1];

        // 如果传入的 params（图标绘制参数）为 null，则创建一个新的 PreviewItemDrawingParams 对象，使用 transX、transY 和缩放比例 totalScale
        if (params == null) {
            params = new PreviewItemDrawingParams(transX, transY, totalScale);
        } else {  // 否则，更新现有的 params 对象，更新它的平移和缩放信息
            params.update(transX, transY, totalScale);
        }
        return params;
    }

    /**
     * 生成文件夹预览的网格布局，根据行 (row) 和列 (col) 来计算图标在预览中的位置
     *
     * Builds a grid based on the positioning of the items when there are
     * {@link #MAX_NUM_ITEMS_IN_PREVIEW} in the preview.
     * <p>
     * Positions in the grid: 0 1  // 0 is row 0, col 1
     *                        2 3  // 3 is row 1, col 1
     */
    private void getGridPosition(int row, int col, float[] result) {
        LogUtils.d("@@@ Marco", "getGridPosition()");
        // We use position 0 and 3 to calculate the x and y distances between items.
        // 通过调用 getPosition 方法，获取第一个图标（位置 0）的坐标，将其存入 result 数组中。这个坐标用于后续的基准位置计算。
        getPosition(0, 4, result);
        // 分别将计算得到的 result 坐标存入 left 和 top，用于之后计算网格的位置。
        float left = result[0];
        float top = result[1];
        LogUtils.d("@@@ Marco", "left = " + left + ", top = " + top);

        // 获取网格中最后一个图标（位置 3）的坐标，这个值用于计算图标之间的 x 和 y 轴上的距离。
        getPosition(3, 4, result);
        // 计算两个图标在 x 和 y 方向上的距离差，即图标之间的间距。
        float dx = result[0] - left;
        float dy = result[1] - top;

        // 根据行和列的位置，通过前面计算出的间距差 dx 和 dy，得出当前图标应该在网格中的精确位置。
        result[0] = left + (col * dx);
        result[1] = top + (row * dy);
    }

    // 根据图标的索引 index 和当前图标总数 curNumItems 计算图标在圆形布局中的位置
    private void getPosition(int index, int curNumItems, float[] result) {
        // 确保当前显示的图标数量至少为 2
        curNumItems = Math.max(curNumItems, 2);

        // We model the preview as a circle of items starting in the appropriate piece of the
        // upper left quadrant (to achieve horizontal and vertical symmetry).
        double theta0 = mIsRtl ? 0 : Math.PI;

        // In RTL we go counterclockwise
        int direction = mIsRtl ? 1 : -1;

        double thetaShift = 0;
        if (curNumItems == 3) {
            thetaShift = Math.PI / 2;
        } else if (curNumItems == 4) {
            thetaShift = Math.PI / 4;
        }
        theta0 += direction * thetaShift;

        // We want the items to appear in reading order. For the case of 1, 2 and 3 items, this
        // is natural for the circular model. With 4 items, however, we need to swap the 3rd and
        // 4th indices to achieve reading order.
        if (curNumItems == 4 && index == 3) {
            index = 2;
        } else if (curNumItems == 4 && index == 2) {
            index = 3;
        }

        // We bump the radius up between 0 and MAX_RADIUS_DILATION % as the number of items increase
        float radius = mRadius * (1 + MAX_RADIUS_DILATION * (curNumItems -
                MIN_NUM_ITEMS_IN_PREVIEW) / (MAX_NUM_ITEMS_IN_PREVIEW - MIN_NUM_ITEMS_IN_PREVIEW));
        double theta = theta0 + index * (2 * Math.PI / curNumItems) * direction;

        float halfIconSize = (mIconSize * scaleForItem(curNumItems)) / 2;

        // Map the location along the circle, and offset the coordinates to represent the center
        // of the icon, and to be based from the top / left of the preview area. The y component
        // is inverted to match the coordinate system.
        result[0] = mAvailableSpace / 2 + (float) (radius * Math.cos(theta) / 2) - halfIconSize;
        result[1] = mAvailableSpace / 2 + (float) (-radius * Math.sin(theta) / 2) - halfIconSize;

    }

    // 根据文件夹中应用图标的数量调整图标的缩放比例。图标越少，比例越大，图标越多，比例越小
    public float scaleForItem(int numItems) {
        // Scale is determined by the number of items in the preview.
        final float scale;
        if (numItems <= 3) {
            scale = MAX_SCALE;
        } else {
            scale = MIN_SCALE;
        }
        return scale * mBaselineIconScale;
    }

    public float getIconSize() {
        return mIconSize;
    }
}
