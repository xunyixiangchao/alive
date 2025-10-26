package com.example.alive.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.example.alive.data.entity.CircleSelection
import kotlin.math.sqrt

/**
 * CircleDrawingImageView - 支持手绘圆圈的自定义ImageView
 *
 * 用途：
 * - Fragment3中显示ALIVE图片
 * - 用户可通过触摸在图片上绘制红色圆圈来标记人物区域
 *
 * 功能：
 * - 支持多个圆圈的绘制
 * - 实时显示正在绘制的圆圈
 * - 自动保存完成的圆圈
 * - 支持撤销最后一个圆圈
 *
 * 绘制样式：
 * - 圆圈颜色：红色（0xFFFF0000）
 * - 线宽：3像素
 * - 最小半径：10像素（防止误触）
 *
 * 触摸事件处理：
 * - ACTION_DOWN: 记录触摸起点
 * - ACTION_MOVE: 实时计算并绘制半径
 * - ACTION_UP: 保存完成的圆圈
 */
class CircleDrawingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    /**
     * 已完成的圆圈列表
     */
    private val completedCircles = mutableListOf<CircleSelection>()

    /**
     * 当前正在绘制的圆圈（触摸未抬起时）
     */
    private var currentCircle: CircleSelection? = null

    /**
     * 触摸起点坐标
     */
    private var startX = 0f
    private var startY = 0f

    /**
     * 圆圈绘制的Paint对象
     * - 颜色：红色
     * - 线宽：3px
     * - 样式：描边（不填充）
     */
    private val circlePaint = Paint().apply {
        color = 0xFFFF0000.toInt()  // 红色
        strokeWidth = 3f
        style = Paint.Style.STROKE  // 描边样式
        isAntiAlias = true
    }

    /**
     * 处理触摸事件
     *
     * 流程：
     * 1. ACTION_DOWN: 记录起点，初始化currentCircle
     * 2. ACTION_MOVE: 计算半径，实时更新currentCircle，触发重绘
     * 3. ACTION_UP: 验证半径是否足够，保存到completedCircles
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                currentCircle = CircleSelection(startX, startY, 0f)
                true
            }

            MotionEvent.ACTION_MOVE -> {
                // 计算当前手指到起点的距离（半径）
                val radius = sqrt(
                    (event.x - startX) * (event.x - startX) +
                    (event.y - startY) * (event.y - startY)
                )

                // 更新当前绘制的圆圈
                currentCircle = CircleSelection(startX, startY, radius)

                // 触发重绘，在onDraw中绘制圆圈
                invalidate()
                true
            }

            MotionEvent.ACTION_UP -> {
                // 获取最终的圆圈
                val circle = currentCircle
                if (circle != null && circle.radius > 10f) {
                    // 半径大于10像素时保存圆圈
                    completedCircles.add(circle)
                }

                // 清除当前绘制的圆圈
                currentCircle = null
                invalidate()
                true
            }

            else -> false
        }
    }

    /**
     * 绘制圆圈
     *
     * 在ImageView的原始内容之上绘制所有的圆圈
     * - completedCircles: 已完成的圆圈（深红色）
     * - currentCircle: 当前正在绘制的圆圈（实时反馈）
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制已完成的圆圈
        for (circle in completedCircles) {
            canvas.drawCircle(
                circle.centerX,
                circle.centerY,
                circle.radius,
                circlePaint
            )
        }

        // 绘制当前正在绘制的圆圈（实时反馈）
        currentCircle?.let { circle ->
            canvas.drawCircle(
                circle.centerX,
                circle.centerY,
                circle.radius,
                circlePaint
            )
        }
    }

    /**
     * 获取所有已完成的圆圈
     * @return 圆圈列表
     */
    fun getCircles(): List<CircleSelection> {
        return completedCircles.toList()
    }

    /**
     * 撤销最后一个圆圈
     */
    fun undoLastCircle() {
        if (completedCircles.isNotEmpty()) {
            completedCircles.removeAt(completedCircles.size - 1)
            invalidate()
        }
    }

    /**
     * 清除所有圆圈
     */
    fun clearAllCircles() {
        completedCircles.clear()
        currentCircle = null
        invalidate()
    }

    /**
     * 检查是否有圆圈
     */
    fun hasCircles(): Boolean = completedCircles.isNotEmpty()
}
