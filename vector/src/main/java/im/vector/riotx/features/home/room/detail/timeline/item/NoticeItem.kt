/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotx.features.home.room.detail.timeline.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import im.vector.riotx.R
import im.vector.riotx.core.ui.views.ReadReceiptsView
import im.vector.riotx.core.utils.DebouncedClickListener
import im.vector.riotx.features.home.AvatarRenderer
import im.vector.riotx.features.home.room.detail.timeline.TimelineEventController

@EpoxyModelClass(layout = R.layout.item_timeline_event_base_noinfo)
abstract class NoticeItem : BaseEventItem<NoticeItem.Holder>() {

    @EpoxyAttribute
    lateinit var avatarRenderer: AvatarRenderer

    @EpoxyAttribute
    var noticeText: CharSequence? = null

    @EpoxyAttribute
    lateinit var informationData: MessageInformationData

    @EpoxyAttribute
    var baseCallback: TimelineEventController.BaseCallback? = null

    private var longClickListener = View.OnLongClickListener {
        return@OnLongClickListener baseCallback?.onEventLongClicked(informationData, null, it) == true
    }

    @EpoxyAttribute
    var readReceiptsCallback: TimelineEventController.ReadReceiptsCallback? = null

    private val _readReceiptsClickListener = DebouncedClickListener(View.OnClickListener {
        readReceiptsCallback?.onReadReceiptsClicked(informationData.readReceipts)
    })

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.noticeTextView.text = noticeText
        avatarRenderer.render(
                informationData.avatarUrl,
                informationData.senderId,
                informationData.memberName?.toString()
                        ?: informationData.senderId,
                holder.avatarImageView
        )
        holder.view.setOnLongClickListener(longClickListener)
        holder.readReceiptsView.render(informationData.readReceipts, avatarRenderer, _readReceiptsClickListener)
        holder.readMarkerView.isVisible = informationData.displayReadMarker
    }

    override fun getViewType() = STUB_ID

    class Holder : BaseHolder(STUB_ID) {
        val avatarImageView by bind<ImageView>(R.id.itemNoticeAvatarView)
        val noticeTextView by bind<TextView>(R.id.itemNoticeTextView)
        val readReceiptsView by bind<ReadReceiptsView>(R.id.readReceiptsView)
        val readMarkerView by bind<View>(R.id.readMarkerView)
    }

    companion object {
        private const val STUB_ID = R.id.messageContentNoticeStub
    }
}