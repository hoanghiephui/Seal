package com.android.video.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TiktokExploreResponse(

	@SerialName("cursor")
	val cursor: String? = null,

	@SerialName("extra")
	val extra: Extra? = null,

	@SerialName("hasMore")
	val hasMore: Boolean? = null,

	@SerialName("status_msg")
	val statusMsg: String? = null,

	@SerialName("itemList")
	val itemList: List<ItemListItem> = emptyList(),

	@SerialName("log_pb")
	val logPb: LogPb? = null,

	@SerialName("statusCode")
	val statusCode: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class ItemControl(

	@SerialName("can_repost")
	val canRepost: Boolean? = null,

	@SerialName("can_music_redirect")
	val canMusicRedirect: Boolean? = null,

	@SerialName("can_comment")
	val canComment: Boolean? = null,

	@SerialName("can_share")
	val canShare: Boolean? = null,

	@SerialName("can_creator_redirect")
	val canCreatorRedirect: Boolean? = null
) : Parcelable

@Parcelize
@Serializable
data class VideoSuggestWordsStructItem(

	@SerialName("words")
	val words: List<WordsItem?>? = null,

	@SerialName("hint_text")
	val hintText: String? = null,

	@SerialName("scene")
	val scene: String? = null
) : Parcelable

@Parcelize
@Serializable
data class ContentsItem(

	@SerialName("textExtra")
	val textExtra: List<TextExtraItem>? = null,

	@SerialName("desc")
	val desc: String? = null
) : Parcelable

@Parcelize
@Serializable
data class StatsV2(

	@SerialName("playCount")
	val playCount: String? = null,

	@SerialName("shareCount")
	val shareCount: String? = null,

	@SerialName("collectCount")
	val collectCount: String? = null,

	@SerialName("diggCount")
	val diggCount: String? = null,

	@SerialName("repostCount")
	val repostCount: String? = null,

	@SerialName("commentCount")
	val commentCount: String? = null
) : Parcelable

@Parcelize
@Serializable
data class Video(

	@SerialName("originCover")
	val originCover: String? = null,

	@SerialName("subtitleInfos")
	val subtitleInfos: List<SubtitleInfosItem>? = null,

	@SerialName("dynamicCover")
	val dynamicCover: String? = null,

	@SerialName("VQScore")
	val vQScore: String? = null,

	@SerialName("format")
	val format: String? = null,

	@SerialName("bitrate")
	val bitrate: Int? = null,

	@SerialName("downloadAddr")
	val downloadAddr: String? = null,

	@SerialName("encodeUserTag")
	val encodeUserTag: String? = null,

	@SerialName("codecType")
	val codecType: String? = null,

	@SerialName("bitrateInfo")
	val bitrateInfo: List<BitrateInfoItem>? = null,

	@SerialName("videoQuality")
	val videoQuality: String? = null,

	@SerialName("cover")
	val cover: String? = null,

	@SerialName("duration")
	val duration: Int? = null,

	@SerialName("zoomCover")
	val zoomCover: ZoomCover? = null,

	@SerialName("playAddr")
	val playAddr: String? = null,

	@SerialName("volumeInfo")
	val volumeInfo: VolumeInfo? = null,

	@SerialName("width")
	val width: Int? = null,

	@SerialName("definition")
	val definition: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("encodedType")
	val encodedType: String? = null,

	@SerialName("height")
	val height: Int? = null,

	@SerialName("ratio")
	val ratio: String? = null
) : Parcelable

@Parcelize
@Serializable
data class WordsItem(

	@SerialName("word_id")
	val wordId: String? = null,

	@SerialName("word")
	val word: String? = null
) : Parcelable

@Parcelize
@Serializable
data class ExtraInfo(

	@SerialName("subtype")
	val subtype: String? = null
) : Parcelable

@Parcelize
@Serializable
data class AuthorStats(

	@SerialName("videoCount")
	val videoCount: Int? = null,

	@SerialName("friendCount")
	val friendCount: Int? = null,

	@SerialName("diggCount")
	val diggCount: Int? = null,

	@SerialName("followerCount")
	val followerCount: Int? = null,

	@SerialName("followingCount")
	val followingCount: Int? = null,

	@SerialName("heartCount")
	val heartCount: Int? = null,

	@SerialName("heart")
	val heart: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class Extra(

	@SerialName("now")
	val now: Long? = null,

	@SerialName("logid")
	val logid: String? = null
) : Parcelable

@Parcelize
@Serializable
data class ItemListItem(

	@SerialName("shareEnabled")
	val shareEnabled: Boolean? = null,

	@SerialName("privateItem")
	val privateItem: Boolean? = null,

	@SerialName("collected")
	val collected: Boolean? = null,

	@SerialName("poi")
	val poi: Poi? = null,

	@SerialName("secret")
	val secret: Boolean? = null,

	@SerialName("video")
	val video: Video? = null,

	@SerialName("authorStats")
	val authorStats: AuthorStats? = null,

	@SerialName("item_control")
	val itemControl: ItemControl? = null,

	@SerialName("music")
	val music: Music? = null,

	@SerialName("stats")
	val stats: Stats? = null,

	@SerialName("duetEnabled")
	val duetEnabled: Boolean? = null,

	@SerialName("officalItem")
	val officalItem: Boolean? = null,

	@SerialName("statsV2")
	val statsV2: StatsV2? = null,

	@SerialName("digged")
	val digged: Boolean? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("diversificationId")
	val diversificationId: Int? = null,

	@SerialName("forFriend")
	val forFriend: Boolean? = null,

	@SerialName("itemCommentStatus")
	val itemCommentStatus: Int? = null,

	@SerialName("author")
	val author: Author? = null,

	@SerialName("duetDisplay")
	val duetDisplay: Int? = null,

	@SerialName("originalItem")
	val originalItem: Boolean? = null,

	@SerialName("stitchDisplay")
	val stitchDisplay: Int? = null,

	@SerialName("AIGCDescription")
	val aIGCDescription: String? = null,

	@SerialName("contents")
	val contents: List<ContentsItem>? = null,

	@SerialName("createTime")
	val createTime: Int? = null,

	@SerialName("stitchEnabled")
	val stitchEnabled: Boolean? = null,

	@SerialName("desc")
	val desc: String? = null,

	@SerialName("challenges")
	val challenges: List<ChallengesItem?>? = null,

	@SerialName("textExtra")
	val textExtra: List<TextExtraItem?>? = null,

	@SerialName("playlistId")
	val playlistId: String? = null,

	@SerialName("isAd")
	val isAd: Boolean? = null,

	@SerialName("videoSuggestWordsList")
	val videoSuggestWordsList: VideoSuggestWordsList? = null,

	@SerialName("anchors")
	val anchors: List<AnchorsItem?>? = null
) : Parcelable

@Parcelize
@Serializable
data class ZoomCover(

	@SerialName("960")
	val jsonMember960: String? = null,

	@SerialName("720")
	val jsonMember720: String? = null,

	@SerialName("480")
	val jsonMember480: String? = null,

	@SerialName("240")
	val jsonMember240: String? = null
) : Parcelable

@Parcelize
@Serializable
data class AnchorsItem(

	@SerialName("schema")
	val schema: String? = null,

	@SerialName("thumbnail")
	val thumbnail: Thumbnail? = null,

	@SerialName("icon")
	val icon: Icon? = null,

	@SerialName("description")
	val description: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("keyword")
	val keyword: String? = null,

	@SerialName("type")
	val type: Int? = null,

	@SerialName("extraInfo")
	val extraInfo: ExtraInfo? = null
) : Parcelable

@Parcelize
@Serializable
data class LogPb(

	@SerialName("impr_id")
	val imprId: String? = null
) : Parcelable

@Parcelize
@Serializable
data class Poi(

	@SerialName("country")
	val country: String? = null,

	@SerialName("fatherPoiId")
	val fatherPoiId: String? = null,

	@SerialName("address")
	val address: String? = null,

	@SerialName("city")
	val city: String? = null,

	@SerialName("cityCode")
	val cityCode: String? = null,

	@SerialName("ttTypeNameMedium")
	val ttTypeNameMedium: String? = null,

	@SerialName("ttTypeCode")
	val ttTypeCode: String? = null,

	@SerialName("ttTypeNameSuper")
	val ttTypeNameSuper: String? = null,

	@SerialName("type")
	val type: Int? = null,

	@SerialName("typeCode")
	val typeCode: String? = null,

	@SerialName("fatherPoiName")
	val fatherPoiName: String? = null,

	@SerialName("province")
	val province: String? = null,

	@SerialName("ttTypeNameTiny")
	val ttTypeNameTiny: String? = null,

	@SerialName("countryCode")
	val countryCode: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("category")
	val category: String? = null
) : Parcelable

@Parcelize
@Serializable
data class PlayAddr(

	@SerialName("FileCs")
	val fileCs: String? = null,

	@SerialName("UrlKey")
	val urlKey: String? = null,

	@SerialName("DataSize")
	val dataSize: Int? = null,

	@SerialName("FileHash")
	val fileHash: String? = null,

	@SerialName("Height")
	val height: Int? = null,

	@SerialName("Uri")
	val uri: String? = null,

	@SerialName("UrlList")
	val urlList: List<String?>? = null,

	@SerialName("Width")
	val width: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class Music(

	@SerialName("duration")
	val duration: Int? = null,

	@SerialName("coverThumb")
	val coverThumb: String? = null,

	@SerialName("original")
	val original: Boolean? = null,

	@SerialName("authorName")
	val authorName: String? = null,

	@SerialName("coverMedium")
	val coverMedium: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("coverLarge")
	val coverLarge: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("playUrl")
	val playUrl: String? = null
) : Parcelable

@Parcelize
@Serializable
data class SubtitleInfosItem(

	@SerialName("Format")
	val format: String? = null,

	@SerialName("UrlExpire")
	val urlExpire: Int? = null,

	@SerialName("LanguageCodeName")
	val languageCodeName: String? = null,

	@SerialName("Version")
	val version: String? = null,

	@SerialName("Size")
	val size: Int? = null,

	@SerialName("LanguageID")
	val languageID: String? = null,

	@SerialName("Source")
	val source: String? = null,

	@SerialName("Url")
	val url: String? = null
) : Parcelable

@Parcelize
@Serializable
data class ChallengesItem(

	@SerialName("coverLarger")
	val coverLarger: String? = null,

	@SerialName("coverThumb")
	val coverThumb: String? = null,

	@SerialName("coverMedium")
	val coverMedium: String? = null,

	@SerialName("profileMedium")
	val profileMedium: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("profileLarger")
	val profileLarger: String? = null,

	@SerialName("desc")
	val desc: String? = null,

	@SerialName("profileThumb")
	val profileThumb: String? = null
) : Parcelable

@Parcelize
@Serializable
data class TextExtraItem(

	@SerialName("start")
	val start: Int? = null,

	@SerialName("end")
	val end: Int? = null,

	@SerialName("hashtagName")
	val hashtagName: String? = null,

	@SerialName("isCommerce")
	val isCommerce: Boolean? = null,

	@SerialName("subType")
	val subType: Int? = null,

	@SerialName("type")
	val type: Int? = null,

	@SerialName("awemeId")
	val awemeId: String? = null,

	@SerialName("secUid")
	val secUid: String? = null,

	@SerialName("userUniqueId")
	val userUniqueId: String? = null,

	@SerialName("userId")
	val userId: String? = null
) : Parcelable

@Parcelize
@Serializable
data class Stats(

	@SerialName("playCount")
	val playCount: Int? = null,

	@SerialName("shareCount")
	val shareCount: Int? = null,

	@SerialName("collectCount")
	val collectCount: Int? = null,

	@SerialName("diggCount")
	val diggCount: Int? = null,

	@SerialName("commentCount")
	val commentCount: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class Icon(

	@SerialName("urlList")
	val urlList: List<String?>? = null
) : Parcelable

@Parcelize
@Serializable
data class VideoSuggestWordsList(

	@SerialName("video_suggest_words_struct")
	val videoSuggestWordsStruct: List<VideoSuggestWordsStructItem>? = null
) : Parcelable

@Parcelize
@Serializable
data class Author(

	@SerialName("avatarMedium")
	val avatarMedium: String? = null,

	@SerialName("isADVirtual")
	val isADVirtual: Boolean? = null,

	@SerialName("downloadSetting")
	val downloadSetting: Int? = null,

	@SerialName("secUid")
	val secUid: String? = null,

	@SerialName("signature")
	val signature: String? = null,

	@SerialName("stitchSetting")
	val stitchSetting: Int? = null,

	@SerialName("commentSetting")
	val commentSetting: Int? = null,

	@SerialName("verified")
	val verified: Boolean? = null,

	@SerialName("privateAccount")
	val privateAccount: Boolean? = null,

	@SerialName("secret")
	val secret: Boolean? = null,

	@SerialName("relation")
	val relation: Int? = null,

	@SerialName("avatarThumb")
	val avatarThumb: String? = null,

	@SerialName("ftc")
	val ftc: Boolean? = null,

	@SerialName("openFavorite")
	val openFavorite: Boolean? = null,

	@SerialName("avatarLarger")
	val avatarLarger: String? = null,

	@SerialName("duetSetting")
	val duetSetting: Int? = null,

	@SerialName("nickname")
	val nickname: String? = null,

	@SerialName("isEmbedBanned")
	val isEmbedBanned: Boolean? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("uniqueId")
	val uniqueId: String? = null,

	@SerialName("roomId")
	val roomId: String? = null
) : Parcelable

@Parcelize
@Serializable
data class VolumeInfo(

	@SerialName("Loudness")
	val loudness: Double? = null,

	@SerialName("Peak")
	val peak: Double? = null
) : Parcelable

@Parcelize
@Serializable
data class BitrateInfoItem(

	@SerialName("GearName")
	val gearName: String? = null,

	@SerialName("QualityType")
	val qualityType: Int? = null,

	@SerialName("PlayAddr")
	val playAddr: PlayAddr? = null,

	@SerialName("MVMAF")
	val mVMAF: String? = null,

	@SerialName("Bitrate")
	val bitrate: Int? = null,

	@SerialName("CodecType")
	val codecType: String? = null
) : Parcelable

@Parcelize
@Serializable
data class Thumbnail(

	@SerialName("width")
	val width: Int? = null,

	@SerialName("urlList")
	val urlList: List<String>? = null,

	@SerialName("height")
	val height: Int? = null
) : Parcelable
