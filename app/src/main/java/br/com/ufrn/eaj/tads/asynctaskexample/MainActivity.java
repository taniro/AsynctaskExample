package br.com.ufrn.eaj.tads.asynctaskexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView img;
    private ProgressBar progresso;
    private Bitmap bitmap;
    private static final String URL = "http://tads.eaj.ufrn.br/projects/tads.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.imagem);
        progresso = (ProgressBar) findViewById(R.id.progresso);

        downloadImagem();

        //Exemplo de como usar um timer para colocar a AsyncTask em loop.
        //NÃO RECOMENDADO -  Usar um serviço !
        /*
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadImagem();
                    }
                });
            }
        }, 0, 10000);
        */
    }

    private void downloadImagem(){
        BaixarArquivosTask tarefa = new BaixarArquivosTask();
        tarefa.execute(URL);
    }

    /*
    *AsyncTask<Params, Progress, Result>
    *      Params: são os argumentos que podemos passar ao método execute( params ...) para executar o AsyncTask
    *      Progress: Pode ser usado para receber um valor inteiro que representa o processo da execução.
    *      Result: É o tipo dos dados que serão retornados pelo doInBackground
    */
    private class BaixarArquivosTask extends AsyncTask<String, Integer, Bitmap>{

        /*
        Método executado antes da Thread inicia, sendo uma boa oportunidade para exibir
        uma janela de progresso para o usuário ou uma mensagem do tipo "por favor, aguarde"
        Esse método executa na UI Thread
         */
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progresso.setVisibility(View.VISIBLE);

        }

        /*
        * Método executado em background por uma thread. Aqui deve estar todo o processamento pesado
        * Esse método pode retornar qualquer objeto. O tipo do objeto é espeficiado no 3º parámetro
        * da classe genérica AsyncTest <1, 2, 3>
        * O dado de retorno será passado como parâmetro para o método onPostExecute()
         */
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                bitmap = loadImageFromNetwork(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /*
        * Método chamado na UI thread e recebe geralmente um inteiro para informar a quantidade do progresso
        * O progresso deve ser informado em background dentro do método doInBackGround(). Para isso,
        * dentro do doInBackgroud é necessário chamar o método publishProgress(int)
         */
        @Override
        protected void onProgressUpdate(Integer ... progress){

        }

        /*
        * Método executado na Ui Thread em que podemos atualizar uma view com o resultado do processamento
        * realizado em background. Ele é implementado internamente usando o handler.
         */
        @Override
        protected void onPostExecute(Bitmap result){
            img.setImageBitmap(result);
            progresso.setVisibility(View.INVISIBLE);

        }
    }

    Bitmap loadImageFromNetwork(String url) throws IOException {
        Bitmap bitmap = null;
        InputStream in = new URL(url).openStream();
        // Converte a InputStream do Java para Bitmap
        bitmap = BitmapFactory.decodeStream(in);
        in.close();
        return bitmap;
    }
}
