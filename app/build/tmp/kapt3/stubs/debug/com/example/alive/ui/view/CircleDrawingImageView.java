package com.example.alive.ui.view;

/**
 * 圈选自定义ImageView
 * 在Fragment3中使用，用于在marked ALIVE图像或8帧上绘制红色圆圈
 * 支持手势交互：用户通过按住-拖动-释放来绘制圆圈
 *
 * 功能特性：
 * - 支持多个圆圈的绘制和管理
 * - 使用红色线条绘制圆圈轮廓（不填充）
 * - 每次绘制最小半径为10px
 * - 支持获取、设置、清空、添加、删除圆圈操作
 * - 使用Canvas.drawCircle()实时渲染圆圈
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0013J\u0006\u0010\u0018\u001a\u00020\u0016J\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00130\u001aJ\u0010\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001dH\u0014J\u0010\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0006\u0010!\u001a\u00020\u0016J\u0014\u0010\"\u001a\u00020\u00162\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u001aR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/alive/ui/view/CircleDrawingImageView;", "Landroid/widget/ImageView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "currentPoint", "Landroid/graphics/PointF;", "currentRadius", "", "isDrawing", "", "paint", "Landroid/graphics/Paint;", "selections", "", "Lcom/example/alive/data/entity/CircleSelection;", "startPoint", "addSelection", "", "selection", "clearSelections", "getSelections", "", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onTouchEvent", "event", "Landroid/view/MotionEvent;", "removeLastSelection", "setSelections", "app_debug"})
public final class CircleDrawingImageView extends android.widget.ImageView {
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint paint = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.alive.data.entity.CircleSelection> selections = null;
    private boolean isDrawing = false;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.PointF startPoint;
    @org.jetbrains.annotations.NotNull()
    private android.graphics.PointF currentPoint;
    private float currentRadius = 0.0F;
    
    @kotlin.jvm.JvmOverloads()
    public CircleDrawingImageView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public CircleDrawingImageView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public CircleDrawingImageView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    /**
     * 设置要显示的圆圈列表
     * 清空现有圆圈，添加新的圆圈列表，并触发UI更新
     *
     * @param selections 要显示的CircleSelection列表
     */
    public final void setSelections(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.alive.data.entity.CircleSelection> selections) {
    }
    
    /**
     * 获取当前已绘制的所有圆圈
     *
     * @return 圆圈列表的副本（防止外部修改）
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.alive.data.entity.CircleSelection> getSelections() {
        return null;
    }
    
    /**
     * 清空所有已绘制的圆圈
     */
    public final void clearSelections() {
    }
    
    /**
     * 添加一个新的圆圈
     *
     * @param selection 要添加的CircleSelection对象
     */
    public final void addSelection(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.CircleSelection selection) {
    }
    
    /**
     * 撤销操作：删除最后添加的一个圆圈
     * 如果没有圆圈则不执行任何操作
     */
    public final void removeLastSelection() {
    }
    
    /**
     * 绘制回调
     * 系统在需要重绘View时调用此方法
     * 首先绘制原始ImageView内容（已加载的图像），然后绘制所有圆圈
     *
     * @param canvas 绘图画布
     */
    @java.lang.Override()
    protected void onDraw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    /**
     * 触摸事件处理
     * 监听用户的按下、拖动、释放操作来绘制圆圈
     *
     * @param event MotionEvent包含触摸坐标和事件类型
     * @return 返回true表示已处理事件，不继续传递
     */
    @java.lang.Override()
    public boolean onTouchEvent(@org.jetbrains.annotations.NotNull()
    android.view.MotionEvent event) {
        return false;
    }
}