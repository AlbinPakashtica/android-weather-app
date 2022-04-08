package com.example.myweatherapp.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.myweatherapp.models.WeatherModel
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "MyGraphView"

class MyGraphView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var dataSet: List<WeatherModel>
    private var dataPoints: ArrayList<DataPoint> = arrayListOf()
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var xSize: Int = 900
    private var ySize: Int = 1000
    private var xUnit: Float = xSize / 7f
    private var yUnit: Float = ySize / 17f
    private var maxTemp: Float = 12f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initMeasurements()
        createDataPoints()
        drawGraphGrid(canvas, paint)
        drawPlotLines(canvas)
    }

    fun setData(weatherModels: List<WeatherModel>) {

        xUnit = xSize / 8f
        yUnit = ySize / 17f
        val items = weatherModels.filter { weatherModel -> weatherModel.type == 1 }
        maxTemp = items.maxOf { it.max }.toFloat()
        dataSet = items
        Log.d("SetData", "items, filtered data: $weatherModels")
        Log.d("SetData", "setData: xunit=$xUnit, yUnit=$yUnit, width=$width, height=$height")
        invalidate()
    }

    private fun initMeasurements() {
        if (dataSet.isNotEmpty() && !dataSet.isNullOrEmpty()) {
            xSize = width
            ySize = height
            xUnit = xSize / dataSet.size.toFloat()
            yUnit = ySize / maxTemp
        } else
            Log.d(TAG, "initMeasurements: DataSet has not been called by recycle view adapter")
    }

    private fun drawGraphGrid(canvas: Canvas?, paint: Paint) {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = Color.CYAN
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 12f
        canvas?.drawLine(xUnit * 0.3f, yUnit, xUnit * 0.3f, ySize - yUnit, paint)
        canvas?.drawLine(xSize.toFloat() + xUnit, ySize - yUnit, xUnit * 0.3f, ySize - yUnit, paint)
    }

    private fun drawPlotLines(canvas: Canvas?) {

        dataPoints.forEachIndexed { index, currentPoint ->

            if (index < dataSet.size - 1) {
                val nextPoint = dataPoints[index + 1]
                canvas?.drawLine(
                    currentPoint.xCord,
                    currentPoint.yMaxCord,
                    nextPoint.xCord,
                    nextPoint.yMaxCord,
                    getMaxTempPaint()
                )
                canvas?.drawLine(
                    currentPoint.xCord,
                    currentPoint.yMinCord,
                    nextPoint.xCord,
                    nextPoint.yMinCord,
                    getMinTempPaint()
                )
                if (currentPoint.yMaxCord > nextPoint.yMaxCord) {
                    drawTempOnPoint(currentPoint, canvas, 40f)
                } else {
                    drawTempOnPoint(currentPoint, canvas, -25f)
                }
                if (currentPoint.yMinCord > nextPoint.yMinCord) {
                    drawTempOnMinPoint(currentPoint, canvas, 40f)
                } else {
                    drawTempOnMinPoint(currentPoint, canvas, -25f)
                }
            }
            if (index == dataSet.size - 1) {
                drawTempOnPoint(currentPoint, canvas, -25f)
                drawTempOnMinPoint(currentPoint, canvas, -25f)
            }

            canvas?.drawCircle(currentPoint.xCord, currentPoint.yMinCord, 12f, getPointPaint())
            canvas?.drawCircle(currentPoint.xCord, currentPoint.yMaxCord, 12f, getPointPaint())
            canvas?.drawText(
                currentPoint.day,
                currentPoint.xCord - xUnit * 0.1f,
                ySize + 40f - yUnit,
                getLegendPaint()
            )

            Log.d(TAG, "drawPlotLines: it=$currentPoint")
            Log.d("SetData", "setData: xunit=$xUnit, yUnit=$yUnit, width=$width, height=$height")
        }
    }

    private fun createDataPoints() {
        var xOrigin = xUnit
        val yOrigin = ySize - yUnit
        val xCompressionFactor = xUnit * 0.45f
        val yCompressionFactor = yUnit * 0.4f
        val yDisplacementFactor = yUnit * 3f
        dataSet.forEach { point ->
            dataPoints.add(
                DataPoint(
                    xOrigin - xCompressionFactor,
                    (yOrigin - yDisplacementFactor - point.max * yCompressionFactor).toFloat(),
                    (yOrigin - yDisplacementFactor - point.min * yCompressionFactor).toFloat(),
                    SimpleDateFormat("EEE", Locale.getDefault()).format(point.dateTime),
                    point.max,
                    point.min
                )
            )
            xOrigin += xUnit
        }
    }

    private fun getLegendPaint(): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.DKGRAY
        paint.textSize = 30f
        return paint
    }

    private fun getPointPaint(): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.strokeWidth = 24f
        return paint
    }

    private fun getMinTempPaint(): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.BLUE
        paint.strokeWidth = 16f
        return paint
    }

    private fun getMaxTempPaint(): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = Color.RED
        paint.strokeWidth = 16f
        return paint
    }

    private fun drawTempOnPoint(point: DataPoint, canvas: Canvas?, offset: Float) {
        canvas?.drawText(
            "${point.maxTemp.toInt()}°C",
            point.xCord,
            point.yMaxCord + offset,
            getLegendPaint()
        )
    }

    private fun drawTempOnMinPoint(point: DataPoint, canvas: Canvas?, offset: Float) {
        canvas?.drawText(
            "${point.minTemp.toInt()}°C",
            point.xCord,
            point.yMinCord + offset,
            getLegendMinPaint()
        )
    }

    private fun getLegendMinPaint(): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.DKGRAY
        paint.textSize = 30f
        return paint
    }

    data class DataPoint(
        val xCord: Float,
        val yMaxCord: Float,
        val yMinCord: Float,
        val day: String,
        val maxTemp: Double,
        val minTemp: Double
    )
}