package com.konovodov.diplomkt.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.konovodov.diplomkt.R
import com.konovodov.diplomkt.dto.Quote
import java.io.File
import java.io.FileOutputStream
import java.util.*


class QuoteRepositoryRoomImpl(private val dao: QuoteDao, private val context: Context) : QuoteRepository {

    override fun getList(): LiveData<List<Quote>> {

        if (dao.getSize() == 0) hardCodeData()

        return Transformations.map(dao.getAll()) { list ->
            list.map {
                Quote(
                        it.id,
                        it.author,
                        it.published,
                        it.content,
                        it.link,
                        it.likes,
                        it.imagePath
                )
            }
        }

    }

    override fun getListByAuthor(author: String) = Transformations.map(dao.getByAuthor(author)) { list ->
        list.map {
            Quote(
                    it.id,
                    it.author,
                    it.published,
                    it.content,
                    it.link,
                    it.likes,
                    it.imagePath
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun dislikeById(id: Long) {
        dao.dislikeById(id)
    }

    override fun shareById(id: Long) {
    }

    override fun saveQuote(quote: Quote) {

        var quoteToSave = quote.copy()

        while (true) {
            if (quote.imagePath.isNotEmpty()) {
                break;
            }
            quote.imageUri?.let {
                quoteToSave = quote.copy(imagePath = makeImagePathFromUri(it))
                return@let;
            } ?: quote.imageDrawable?.let {
                quoteToSave = quote.copy(imagePath = makeImagePathFromDrawable(it))
                return@let
            }
            break;
        }
        dao.save(QuoteEntity.fromDto(quoteToSave))
    }

    private fun makeImagePathFromUri(uri: Uri): String {
        //здесь нужно взять изображение по uri и сохранить его в папке программы, попутно сгенерировав имя файла и вернуть путь до него

        return ""
    }

    private fun makeImagePathFromDrawable(drawable: Drawable): String {
        //здесь нужно взять изображение и сохранить его в папке программы, попутно сгенерировав имя файла и вернуть путь до него
        val fileName = drawable.hashCode().toString() + ".png"
        val bm = (drawable as BitmapDrawable).bitmap

        val file = context.filesDir.resolve(fileName)
        if(!file.exists()) {
            val outStream = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        }
        return file.absolutePath
    }


    override fun getById(id: Long): Quote {
        if (id == 0L) return getEmptyQuote()
        return dao.getById(id).toDto()
    }

    override fun getEmptyQuote(): Quote = Quote()


    fun hardCodeData() {
        var random: Random = Random(1)

        saveQuote(Quote(author = "Бендер", content = "Давайте смотреть реально. Комедия — мёртвый жанр, а трагедия — это смешно!", likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender1)))
//        saveQuote(Quote(author = "Бендер", content = "Давайте смотреть реально. Комедия — мёртвый жанр, а трагедия — это смешно!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong() - random.nextInt(10000).toLong()))
        saveQuote(Quote(author = "Бендер", content = "Знаешь, что меня подбадривает? Издевательство над чужими неудачами.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender2)))
        saveQuote(Quote(author = "Бендер", content = "На помощь! На помощь! Я слишком ленив, чтобы спасаться!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender3)))
        saveQuote(Quote(author = "Бендер", content = "Не бейте меня!.. Я предам кого угодно!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender1)))
        saveQuote(Quote(author = "Бендер", content = "Не люблю тусоваться! Просто у меня шило в жопе, которого я не просил!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender2)))
        saveQuote(Quote(author = "Бендер", content = "Позёры! Я ненавидел Макаревича ещё до того, как это стало модным.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender3)))
        saveQuote(Quote(author = "Бендер", content = "Ура! Я богат! И ты тоже… Но это почему-то не радует.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender1)))
        saveQuote(Quote(author = "Бендер", content = "Интересно. Нет, не то слово. Скучно.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender2)))
        saveQuote(Quote(author = "Бендер", content = "У меня будет свой луна-парк… С блэкджеком и шлюхами! А хотя к чёрту луна-парк!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender3)))
        saveQuote(Quote(author = "Бендер", content = "В шахматах нельзя показывать противнику свои козыри.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.bender1)))
        saveQuote(Quote(author = "Фрай", content = "Уф! Какой мне страшный сон приснился! Больше никогда не буду спать!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.fry1)))
        saveQuote(Quote(author = "Фрай", content = "Санта, ты спас мне жизнь. Не убивай меня.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.fry2)))
        saveQuote(Quote(author = "Фрай", content = "Класс! Будущее! Моя семья, коллеги, моя девушка… я их больше никогда не увижу… Ура!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.fry3)))
        saveQuote(Quote(author = "Фрай", content = "День святого валентина? о, черт…я опять забыл завести девушку.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.fry1)))
        saveQuote(Quote(author = "Фрай", content = "Почему йогурт в кепке? Я могу все объяснить. Он раньше был молоком, но понимаете — время калечит всех…", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.fry2)))
        saveQuote(Quote(author = "Фарнсворт", content = "Я всегда боялся, что он вот так вот убежит. Почему?! Почему… я не сломал ему ноги?!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.farnsworth)))
        saveQuote(Quote(author = "Фарнсворт", content = "Хорошие новости, народ: по телевизору показывают плохие новости!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.farnsworth2)))
        saveQuote(Quote(author = "Фарнсворт", content = "Бог мой, надо что-то предпринять… но я уже в пижаме.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.farnsworth)))
        saveQuote(Quote(author = "Фарнсворт", content = "Да это же обычная вода….обычная вода с небольшой примесью ЛСД.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.farnsworth2)))
        saveQuote(Quote(author = "Бранниган", content = "Пока я командую, любая миссия суицидальная!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.brannigan)))
        saveQuote(Quote(author = "Бранниган", content = "Гравитация, ты снова победила!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.brannigan)))
        saveQuote(Quote(author = "Бранниган", content = "Я получил ваш вызов о помощи и прибыл, как только захотел.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.brannigan)))
        saveQuote(Quote(author = "Гомер Симпсон", content = "Как много интересного вы говорите! Как жаль, что это меня мало интересует.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.homer1)))
        saveQuote(Quote(author = "Гомер Симпсон", content = "Для вранья нужны двое. Один врет, другой слушает.", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.homer2)))
        saveQuote(Quote(author = "Гомер Симпсон", content = "О, Боже! Космические пришельцы! Не ешьте меня. У меня жена, дети. Съешьте их!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.homer1)))
        saveQuote(Quote(author = "Гомер Симпсон", content = "Вообще-то я не религиозный человек, но если ты есть там наверху, спаси меня, Супермен!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.homer2)))
        saveQuote(Quote(author = "Гомер Симпсон", content = "Ты поможешь мне, а я за это приму помощь от тебя!", likes = random.nextInt(100) - 5, published = 1614766259 - random.nextInt(10000).toLong(), imageDrawable = AppCompatResources.getDrawable(context, R.drawable.homer1)))
    }

}