package fi.metropolia.foobar.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class TransitionActivity extends AppCompatActivity {

    // override start/leave activity actions to add animation.

    @Override
    public void finish() {
        super.finish();
        onLeaveActivityAnimation();
    }

    protected void onLeaveActivityAnimation() {
        overridePendingTransition(R.anim.do_nothing, R.anim.exit_to_right );
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartActivityAnimation();
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        onStartActivityAnimation();
    }


    protected void onStartActivityAnimation() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.do_nothing);
    }

    // add action bar back button transition to all activities.

    //   https://developer.android.com/training/implementing-navigation/ancestral

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}