package fi.metropolia.foobar.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Defines a new alternative Activity class with predefined transition animations, that each activity can extend instead.
 */
public abstract class TransitionActivity extends AppCompatActivity {

    /**
     *   overrides activity finnish method to ensure animation is played.
     */

    @Override
    public void finish() {
        super.finish();
        onLeaveActivityAnimation();
    }

    /**
     * defines animation to display when leaving the activity.
     */
    protected void onLeaveActivityAnimation() {
        overridePendingTransition(R.anim.do_nothing, R.anim.exit_to_right );
    }

    /**
     * catches starting activity and redefines the animation to display.
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartActivityAnimation();
    }

    /**
     * catches leaving activity and redefines the animation to display.
     * @param intent
     */

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        onStartActivityAnimation();
    }

    /**
     * define an animation to call for starting activity.
     */

    protected void onStartActivityAnimation() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.do_nothing);
    }

    /**
     * add action bar back button transition to all activities.
     * Action bar back button is a menu item like any other, this listener checks for only this ( home ) button and responds to it
     * otherwise passes the event up the chain to next handler.
     * https://developer.android.com/training/implementing-navigation/ancestral
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();  // ensure finish gets called even if use action bar button to go to parent.
                return true; // we have handled the menu interaction, stop the chain.
        }
        // we did not handle the event, pass it up the chain.
        return super.onOptionsItemSelected(item);
    }
}