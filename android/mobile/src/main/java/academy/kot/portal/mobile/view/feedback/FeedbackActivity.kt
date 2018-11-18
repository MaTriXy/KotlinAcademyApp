package academy.kot.portal.mobile.view.feedback

import academy.kot.portal.android.di.FeedbackRepositoryDi
import academy.kot.portal.mobile.R
import academy.kot.portal.mobile.view.BaseActivity
import activitystarter.Arg
import activitystarter.MakeActivityStarter
import android.app.Activity
import android.os.Bundle
import com.marcinmoskala.activitystarter.argExtra
import com.marcinmoskala.kotlinandroidviewbindings.bindToVisibility
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.coroutines.experimental.android.UI
import org.kotlinacademy.data.Feedback
import org.kotlinacademy.presentation.feedback.FeedbackPresenter
import org.kotlinacademy.presentation.feedback.FeedbackView

@MakeActivityStarter(includeStartForResult = true)
class FeedbackActivity : BaseActivity(), FeedbackView {

    @get:Arg(optional = true)
    val newsId: Int? by argExtra()

    override var loading: Boolean by bindToVisibility(R.id.loadingView)

    private val feedbackRepository by FeedbackRepositoryDi.lazyGet()
    private val presenter by presenter { FeedbackPresenter(UI, this, feedbackRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val titleId =
                if (newsId == null) R.string.feedback_general_title
                else R.string.feedback_article_title
        titleView.setText(titleId)

        sendButton.setOnClickListener {
            sentFilledData()
        }
    }

    override fun backToNewsAndShowSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun sentFilledData() {
        val rating = (ratingView.rating * 2).toInt() + 1
        val commentText = commentView.text.toString()
        val suggestionsText = suggestionsView.text.toString()
        val feedback = Feedback(newsId, rating, commentText, suggestionsText)
        presenter.onSendCommentClicked(feedback)
    }
}
