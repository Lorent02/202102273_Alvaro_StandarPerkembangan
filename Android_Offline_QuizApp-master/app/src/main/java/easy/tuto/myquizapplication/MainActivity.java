package easy.tuto.myquizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String namaAnak;
    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB;
    Button submitBtn;
    int questionNumber = 0;
    int[] motorik2 = {3,3,2,1,1,2,3,3,3,1,2,3,3,1,1,3,1,3,2,1,1,1,1,2,1};
    int[] kognitif2 = {3,3,3,1,1,1,2,3,2,1,2,1,1,3,3,3,1,1,3,2,2,3,3,3,3};
    int[] bahasa2 = {3,3,3,2,2,1,3,3,3,1,1,3,3,2,2,2,1,3,3,3,3,1,2,1,2};
    int[] sosial2 = {3,3,3,1,1,2,3,3,3,2,2,1,3,3,2,1,2,3,1,1,1,2,2,2,3};
    int[] hasil2 = {2,2,2,1,1,1,2,2,2,1,1,1,2,1,1,2,1,2,2,2,2,1,1,1,2};
    double PNormal = 0.52;
    double PTerlambat = 0.48;

    //0 = normal, 1 = terlambat
    double[][] motorik = {{0.615384615, 0.076923077},
            {0.153846154, 0.230769231},
            {0.230769231, 0.615384615}};
    double[][] kognitif = {{0.538461538, 0.384615385},
            {0.307692308, 0.076923077},
            {0.153846154, 0.461538462}};
    double[][] bahasa = {{0.846153846, 0.076923077},
            {0.153846154, 0.384615385},
            {0, 0.461538462}};
    double[][] sosial = {{0.692307692, 0.076923077},
            {0, 0.615384615},
            {0.0,307692308, 0.230769231}};
    String selectedAnswer = "";

    // 1 = motorik, 2 = kognitif, 3 = bahasa, 4 = emosional
    int score1 = 0, score2 = 0, score3 = 0, score4 = 0;
    double persen1, persen2, persen3, persen4;
    int input1, input2, input3, input4;
    int totalQuestion = QuestionAnswer.question.length;
    int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalQuestionsTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_yes);
        ansB = findViewById(R.id.ans_no);
        submitBtn = findViewById(R.id.submit_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {

        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);


        Button clickedButton = (Button) view;
        if (clickedButton.getId() == R.id.submit_btn) {
            //if(selectedAnswer.equals(QuestionAnswer.correctAnswers[currentQuestionIndex])){
            //score++;
            //}

            if (selectedAnswer.equals("Yes")) {
                String kategori=totalQuestionsTextView.getText().toString();
                if (kategori.equals("Fisik Motorik")) {
                    score1++;
                } else if (kategori.equals("Kognitif")) {
                    score2++;
                }else if (kategori.equals("Bahasa")) {
                    score3++;
                }else if (kategori.equals("Sosial Emosional")) {
                    score4++;
                }
            }
            currentQuestionIndex++;
            loadNewQuestion();
        } else {
                //choices button clicked
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);
            }
        }

    void loadNewQuestion(){

        if(currentQuestionIndex == totalQuestion ){
            finishQuiz();
            return;
        }
        questionNumber++;
        if (questionNumber == 21){

            totalQuestionsTextView.setText(QuestionAnswer.kategori[1]);
        }
        if (questionNumber == 38){

            totalQuestionsTextView.setText(QuestionAnswer.kategori[2]);
        }
        if (questionNumber == 56){

            totalQuestionsTextView.setText(QuestionAnswer.kategori[3]);
        }

        questionTextView.setText(QuestionAnswer.question[currentQuestionIndex]);
        ansA.setText(QuestionAnswer.choices[0][0]);
        ansB.setText(QuestionAnswer.choices[0][1]);

    }

    void finishQuiz(){
        persen1 = ((double) score1/20) * 100;
        //Log.d("TAG", String.valueOf(persen1));
        persen2 = ((double) score2/17) * 100;
        //Log.d("TAG", String.valueOf(persen2));
        persen3 = ((double) score3/18) * 100;
        //Log.d("TAG", String.valueOf(persen3));
        persen4 = ((double) score4/16) * 100;
        //Log.d("TAG", String.valueOf(persen4));

        klasifikasi();

        double xNormal = PNormal * motorik[input1][0] * kognitif[input2][0] * bahasa[input3][0] * sosial[input4][0];
        double xTerlambat = PTerlambat * motorik[input1][1] * kognitif[input2][1] * bahasa[input3][1] * sosial[input4][1];

        Log.d("TAG", "" + motorik[input1][0] + kognitif[input2][0] + bahasa[input3][0] + sosial[input4][0]);
        Log.d("TAG", "" + motorik[input1][1] + kognitif[input2][1] + bahasa[input3][1] + sosial[input4][1]);

        double yNormal = xNormal/((double) xNormal + xTerlambat);
        double yTerlambat = xTerlambat/((double) xTerlambat + xNormal);
        Log.d("TAG", "finishQuiz: " + yNormal + " dan " + yTerlambat);
        String hasilakhir;
        if (yNormal > yTerlambat) {

            hasilakhir = "Normal";
        } else {
            hasilakhir = "Terlambat";
        }

        new AlertDialog.Builder(this)
                .setTitle("Hasil")
                .setMessage(namaAnak + hasilakhir)
                .setPositiveButton("Restart",(dialogInterface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();
    }

    void restartQuiz(){
        //score = 0;
        currentQuestionIndex =0;
        totalQuestionsTextView.setText(QuestionAnswer.kategori[0]);
        loadNewQuestion();
    }

    Integer klas1(double x ){
        if(x<=30){
            return 2;
        } else if (x<=50){
            return 1;
        } else {
            return 0;
        }
    }

    void klasifikasi(){
        input1 = klas1(persen1);
        Log.d("INPUT", "klasifikasi: " + input1);
        input2 = klas1(persen2);
        Log.d("INPUT", "klasifikasi: " + input2);
        input3 = klas1(persen3);
        Log.d("INPUT", "klasifikasi: " + input3);
        input4 = klas1(persen4);
        Log.d("INPUT", "klasifikasi: " + input4);
    }
}