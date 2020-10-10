package com.pmid.printbluetooth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.ImagePrintable;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PrintingCallback {


    Printing printing;
    Button btnPrint,btnPrintImages,btnPairUnpair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrint = findViewById(R.id.btnPrint);
        btnPrintImages = findViewById(R.id.btnPrintImages);
        btnPairUnpair = findViewById(R.id.btnPiarUnpair);

        if(printing != null){
            printing.setPrintingCallback(this);
        }
        btnPairUnpair.setOnClickListener(v -> {
            if(Printooth.INSTANCE.hasPairedPrinter()){
                Printooth.INSTANCE.removeCurrentPrinter();
            }else {
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
//                changePairAndUnderpair();
                Toast.makeText(getApplicationContext(),"Mausk",Toast.LENGTH_LONG).show();
            }
        });


        btnPrintImages.setOnClickListener(v -> {
            if(!Printooth.INSTANCE.hasPairedPrinter()){
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
            }else{
                printImages();
            }
        });

        btnPrint.setOnClickListener(v -> {
            if(!Printooth.INSTANCE.hasPairedPrinter()){
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
            }else{
                printText();
            }
        });

        changePairAndUnderpair();
    }

    private void printText() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());

        printables.add(new TextPrintable.Builder()
                .setText("Hello wordhhhjh")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("Hello uhsiudsaijds wwijdsf")
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                .setNewLinesAfter(1)
                .build());

        printing.print(printables);
    }

    private void printImages() {
        ArrayList<Printable> printables = new ArrayList<>();

        Picasso.get()
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQvBeP7gvGz4R4O0UwWNTkFtCzJjW6RaHaYlQLFAwt2ZusmlTfh")
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        printables.add(new ImagePrintable.Builder(bitmap).build());
                        printing.print(printables);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }

    private void changePairAndUnderpair() {
        if(Printooth.INSTANCE.hasPairedPrinter()){
            btnPairUnpair.setText(new StringBuilder("Unpair").append(Printooth.INSTANCE.getPairedPrinter().getName().toString()));
        }else {
            btnPairUnpair.setText("Pair with Printer");
        }
    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(getApplicationContext(),"Connecting to printer",Toast.LENGTH_LONG).show();
    }

    @Override
    public void connectionFailed(String s) {
        Toast.makeText(getApplicationContext(),"Failed "+s,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onError(String s) {
        Toast.makeText(getApplicationContext(),"Error "+s,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMessage(String s) {
        Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(getApplicationContext(),"Order aent to printer",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ScanningActivity.SCANNING_FOR_PRINTER && requestCode == Activity.RESULT_OK){
            initPrinting();
            changePairAndUnderpair();

        }
    }

    private void initPrinting() {
        if(!Printooth.INSTANCE.hasPairedPrinter()){
            printing = Printooth.INSTANCE.printer();
        }
        if(printing != null){
            printing.setPrintingCallback(this);
        }
    }
}