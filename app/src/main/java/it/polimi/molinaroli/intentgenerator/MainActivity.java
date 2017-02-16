package it.polimi.molinaroli.intentgenerator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppCompatActivity {

    Button mail;
    Button web;
    Button photo;
    Context c;
    private String m_Text = "";
    private String sTo = "";
    private String sObject = "";
    private String sText = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mail = (Button) findViewById(R.id.mailIntent);
        web = (Button) findViewById(R.id.webIntent);
        photo = (Button) findViewById(R.id.photoIntent);
        c=this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        prepareWeb();

        prepareMail();

        preparePhoto();

    }

    public void prepareWeb(){
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Insert a valid url to open");

                // Set up the input
                final EditText input = new EditText(c);
                input.setText("http://");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                builder.setView(input);


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        //a  sto punto genero l'intento implicito e forzo il selettore
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                        viewIntent.setData(Uri.parse(m_Text));
                        // Always use string resources for UI text.
                        // This says something like "Share this photo with"
                        String title = "Open page with:";
                        // Create intent to show the chooser dialog
                        Intent chooser = Intent.createChooser(viewIntent, title);

                        // Verify the original intent will resolve to at least one activity
                        if (viewIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(chooser);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void preparePhoto(){
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d("Intent action", cameraIntent.getAction());
                // Always use string resources for UI text.
                // This says something like "Share this photo with"
                String title = "Take the photo with:";
                // Create intent to show the chooser dialog
                Intent chooser = Intent.createChooser(cameraIntent, title);

                // Verify the original intent will resolve to at least one activity
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });
    }

    public void prepareMail(){
        final LayoutInflater inflater = getLayoutInflater();
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Insert data to send email");


                View dialogView = inflater.inflate(R.layout.dialog_mail, null);
                builder.setView(dialogView);

                final EditText to = (EditText) dialogView.findViewById(R.id.to);
                final EditText object = (EditText) dialogView.findViewById(R.id.object);
                final EditText text = (EditText) dialogView.findViewById(R.id.text);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sTo = to.getText().toString();
                        sObject = object.getText().toString();
                        sText = text.getText().toString();
                        //a questo punto posso costruire l'intento per inviare la mail


                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/html");
                        // The intent does not have a URI, so declare the "text/plain" MIME type
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, sTo); // recipients
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sObject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, sText);

                        String title = "Send email with:";
                        // Create intent to show the chooser dialog
                        Intent chooser = Intent.createChooser(emailIntent, title);

                        // Verify the original intent will resolve to at least one activity
                        if (emailIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(chooser);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
}
