package com.example.spritealongpath;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ScenceView extends View {

    // Спрайт
    private static Bitmap bmSprite_01;
    private static Bitmap bmSprite_02;
    // Фоновый рисунок
    private static Bitmap bmBackground;
    private static Rect rSrc, rDest;

    // Шаг анимации
    private static int iMaxAnimationStep_01 = 1000;
    private static int iMaxAnimationStep_02 = 700;
    private int iCurStep = 0;

    // Флаг для отрисовки Path
    private boolean flagDrowPath = false;

    // Точки определяющие кривую
    private List<PointF> aPoints = new ArrayList<PointF>();
    private List<PointF> bPoints = new ArrayList<PointF>();
    private Paint paint_01;
    private Paint paint_02;
    // Кривая
    private Path path_01 = new Path();
    private Path path_02 = new Path();
    //
    private PathMeasure pm_01;    // curve measure
    private PathMeasure pm_02;    // curve measure
    // Длина сегмента кривой
    private float fSegmentLen;

    // Подготавливаем данные для рисования. Само рисование
    // выполняется в методе onDraw
    public ScenceView(Context context) {
        super(context);

        // Определяем размер окна для вывода изображения
        Display display = ((WindowManager)
            context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        rDest = new Rect(0, 0, width, height);

        // Загрузка фонового рисунка
        if(bmBackground == null) {
            bmBackground = BitmapFactory.decodeResource(getResources(),
                    R.drawable.aquarium_background);
            rSrc = new Rect(0,0, bmBackground.getWidth(),
                    bmBackground.getHeight());
        }
        /////////////////////////////////////////////////////////////////////////
        // Загрузка спрайта №1
        if (bmSprite_01 == null)
            bmSprite_01 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.fish_one);

        // Инициализация набора точек (красный цвет)
        aPoints.add(new PointF(10f, 260f));
        aPoints.add(new PointF(500f, 20f));
        aPoints.add(new PointF(700f, 500f));
        aPoints.add(new PointF(900, 300));
        aPoints.add(new PointF(1200, 100));
        aPoints.add(new PointF(2000, 800));
        aPoints.add(new PointF(2800, 200));  // точка за пределами экрана

        aPoints.add(new PointF(10f, 260f));
        aPoints.add(new PointF(500f, 20f));
        aPoints.add(new PointF(700f, 500f));
        aPoints.add(new PointF(900, 300));
        aPoints.add(new PointF(1200, 100));
        aPoints.add(new PointF(2000, 800));
        aPoints.add(new PointF(2800, 200));  // точка за пределами экрана

        /////////////////////////////////////////////////////////////////////////
        // Загрузка спрайта №2
        if (bmSprite_02 == null)
            bmSprite_02 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.fish_two);

        // Инициализация набора точек (красный цвет)
        bPoints.add(new PointF(10f, 550));
        bPoints.add(new PointF(500f, 300));
        bPoints.add(new PointF(700f, 400));
        bPoints.add(new PointF(900, 300));
        bPoints.add(new PointF(1200, 900));
        bPoints.add(new PointF(2000, 700));
        bPoints.add(new PointF(2800, 800));  // точка за пределами экрана


        ////////////////////////////////////////////////////////// #1
        // Выполняем подготовку Path для последующего рисования
        // Определяем точку начало рисования.
        PointF point = aPoints.get(0);
        // Устанавливаем в эту точку графический курсор,
        // далее, рисование пойдет от неё
        path_01.moveTo(point.x, point.y);
        // Следующая точка
        PointF nextPoint;
        for(int i = 0; i < aPoints.size()-1; i++) {
            // Две координаты точки для построения кривой
            point = aPoints.get(i);
            nextPoint = aPoints.get(i+1);
            // Задаем в квадратичном path точки, по которым в дальнейшем
            // будет строится кривая (зеленая линия)
            path_01.quadTo(point.x, point.y,
                    (nextPoint.x + point.x) / 2, (point.y + nextPoint.y) / 2);
        }

        // Создание объекта PathMeasure
        pm_01 = new PathMeasure(path_01, false);
        // Изменение длины сегмента на пути, который должен пройти
        // спрайт за одну итерацию
        fSegmentLen = pm_01.getLength() / iMaxAnimationStep_01; // 20 animation steps

        // Инициализация объекта рисования
        // Включение сглаживания
        paint_01 = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Рисуем только контуры
        paint_01.setStyle(Paint.Style.STROKE);
        // Толщина линии 3
        paint_01.setStrokeWidth(3);
        // Цвет линии
        paint_01.setColor(Color.rgb(0, 148, 255));


        ////////////////////////////////////////////////////////// #2
        // Выполняем подготовку Path для последующего рисования
        // Определяем точку начало рисования.
        PointF point_02 = bPoints.get(0);
        // Устанавливаем в эту точку графический курсор,
        // далее, рисование пойдет от неё
        path_02.moveTo(point.x, point.y);
        // Следующая точка
        PointF nextPoint_02;
        for(int i = 0; i < bPoints.size()-1; i++) {
            // Две координаты точки для построения кривой
            point = bPoints.get(i);
            nextPoint_02 = bPoints.get(i+1);
            // Задаем в квадратичном path точки, по которым в дальнейшем
            // будет строится кривая (зеленая линия)
            path_02.quadTo(point.x, point.y,
                    (nextPoint_02.x + point.x) / 2, (point.y + nextPoint_02.y) / 2);
        }

        // Создание объекта PathMeasure
        pm_02 = new PathMeasure(path_02, false);
        // Изменение длины сегмента на пути, который должен пройти
        // спрайт за одну итерацию
        fSegmentLen = pm_02.getLength() / iMaxAnimationStep_02; // 20 animation steps

        // Инициализация объекта рисования
        // Включение сглаживания
        paint_02 = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Рисуем только контуры
        paint_02.setStyle(Paint.Style.STROKE);
        // Толщина линии 3
        paint_02.setStrokeWidth(3);
        // Цвет линии
        paint_02.setColor(Color.rgb(0, 148, 255));
   }

    // Метод, выполняющий рисование фонового рисунка, Path и спрайта
    // Рисование выполняем на Canvas
    @Override
    protected void onDraw(Canvas canvas) {


        // Рисуем фоновое изображение
        canvas.drawBitmap(bmBackground, rSrc, rDest, null);
        // Рисуем Path (кривую)
        if(flagDrowPath) {
            canvas.drawPath(path_01, paint_01);
            // Рисуем точки
            paint_01.setColor(Color.YELLOW);   // Устанавливаем цвет рисования
            paint_01.setStrokeWidth(5);     // Устанавливаем толщину линии
            for (int i = 0; i < aPoints.size(); i++) {
                canvas.drawCircle(aPoints.get(i).x, aPoints.get(i).y, 10, paint_01);
            }
            // canvas.drawLine(0,0, 1794, 1080-80 paint);
            paint_01.setColor(Color.BLACK);   // Устанавливаем цвет рисования.
            // canvas.drawLine(0,0, 2040, 1080, paint);
        }

        if(flagDrowPath) {
            canvas.drawPath(path_02, paint_02);
            // Рисуем точки
            paint_02.setColor(Color.BLUE);   // Устанавливаем цвет рисования
            paint_02.setStrokeWidth(5);     // Устанавливаем толщину линии
            for (int i = 0; i < bPoints.size(); i++) {
                canvas.drawCircle(bPoints.get(i).x, bPoints.get(i).y, 10, paint_02);
            }
            // canvas.drawLine(0,0, 1794, 1080-80 paint);
            paint_02.setColor(Color.GREEN);   // Устанавливаем цвет рисования.
            // canvas.drawLine(0,0, 2040, 1080, paint);
        }


        // Анимация спрайта #1
        Matrix mxTransform_01 = new Matrix();
        if (iCurStep <= iMaxAnimationStep_01) {
            pm_01.getMatrix(fSegmentLen * iCurStep, mxTransform_01,
                        PathMeasure.POSITION_MATRIX_FLAG +
                        PathMeasure.TANGENT_MATRIX_FLAG);
            mxTransform_01.preTranslate(-bmSprite_01.getWidth(), -
                    bmSprite_01.getHeight());
            canvas.drawBitmap(bmSprite_01, mxTransform_01, null);

            iCurStep++;  // Переход к следующему шагу
            // Объявление данных в окне недействительными.
            // Рисование анимации выполняется в методе onDraw
            invalidate();
        } else {
            iCurStep = 0;
        }

        // Анимация спрайта #2
        Matrix mxTransform_02 = new Matrix();
        if (iCurStep <= iMaxAnimationStep_02) {
            pm_02.getMatrix(fSegmentLen * iCurStep, mxTransform_02,
                    PathMeasure.POSITION_MATRIX_FLAG +
                            PathMeasure.TANGENT_MATRIX_FLAG);
            mxTransform_02.preTranslate(-bmSprite_02.getWidth(), -
                    bmSprite_02.getHeight());
            canvas.drawBitmap(bmSprite_02, mxTransform_02, null);

            iCurStep++;  // Переход к следующему шагу
            // Объявление данных в окне недействительными.
            // Рисование анимации выполняется в методе onDraw
            invalidate();
        } else {
            iCurStep = 0;
        }

    }



    // Функция, обрабатывающая касание пальцем экрана
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            // Объявлем ланные недействительными и начинаем анимацию
            // Рисование анимации выполняется в методе onDraw
            if(flagDrowPath)
                flagDrowPath = false;
            else
                flagDrowPath = true;
            invalidate();
            return true;
        }
        return false;
    }

}
