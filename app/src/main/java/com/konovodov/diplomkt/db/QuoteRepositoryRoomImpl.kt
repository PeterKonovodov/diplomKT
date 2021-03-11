package com.konovodov.diplomkt.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.konovodov.diplomkt.R
import com.konovodov.diplomkt.dto.Quote
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap


class QuoteRepositoryRoomImpl(private val dao: QuoteDao, private val context: Context) :
    QuoteRepository {

    private val imagesCache = HashMap<String, Drawable>()

    override fun getAllPaged(pageSize: Int): LiveData<PagedList<QuoteEntity>> {
        if (dao.getSize() == 0) hardCodeData()
        val c = dao.getAllPaged().toLiveData(
            pageSize = pageSize,
            boundaryCallback = object : PagedList.BoundaryCallback<QuoteEntity>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                }

                override fun onItemAtFrontLoaded(itemAtFront: QuoteEntity) {
                    super.onItemAtFrontLoaded(itemAtFront)
                }

                override fun onItemAtEndLoaded(itemAtEnd: QuoteEntity) {
                    super.onItemAtEndLoaded(itemAtEnd)
                }
            })
        return c
    }

    override fun getAllByAuthorPaged(
        pageSize: Int,
        author: String
    ): LiveData<PagedList<QuoteEntity>> {
        return dao.getAllByAuthorPaged(author).toLiveData(pageSize = pageSize)
    }


    override fun loadImageByPath(path: String): Drawable? {
        if (path.isEmpty()) return null
        if (imagesCache.contains(path)) return imagesCache[path]
        val drawable = BitmapDrawable.createFromPath(path)
        drawable?.let { imagesCache.put(path, it) }
        return drawable
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun dislikeById(id: Long) {
        dao.dislikeById(id)
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        imagesCache.remove(id)
    }

    override fun shareQuote(quote: Quote) {
        if (quote.author == context.resources.getString(R.string.user_name)) return //у себя не плагиатим

        if (quote.fromAuthor.isEmpty()) {        //плагиатим оригинал
            saveQuote(
                quote.copy(
                    id = 0, author = context.resources.getString(R.string.user_name),
                    fromAuthor = quote.author, likes = 0
                )
            )
        }           //плагиатим уже сплагиаченное, это защита от подмены имени
        else saveQuote(
            quote.copy(
                id = 0, author = context.resources.getString(R.string.user_name),
                fromAuthor = quote.fromAuthor, likes = 0
            )
        )

    }

    override fun saveQuote(quote: Quote) {
        dao.save(QuoteEntity.fromDto(quote))
    }



    private fun Drawable?.toFile(): String {
        if(this == null) return ""

        val fileName = this.hashCode().toString() + ".png"
        val bm = (this as BitmapDrawable).bitmap

        val file = context.filesDir.resolve(fileName)
        if (!file.exists()) {
            val outStream = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        }
        return file.absolutePath
    }

    override fun saveImage(drawable: Drawable): String {
        return drawable.toFile()
    }


    override fun getById(id: Long): Quote {
        if (id == 0L) return getEmptyQuote()
        return dao.getById(id).toDto()
    }

    override fun getEmptyQuote(): Quote = Quote()


    private fun hardCodeData() {
        val random = Random(1)

        saveQuote(
            Quote(
                author = "Бендер",
                content = "Давайте смотреть реально. Комедия — мёртвый жанр, а трагедия — это смешно!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "Знаешь, что меня подбадривает? Издевательство над чужими неудачами.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "На помощь! На помощь! Я слишком ленив, чтобы спасаться!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender3).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "Не бейте меня!.. Я предам кого угодно!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "Не люблю тусоваться! Просто у меня шило в жопе, которого я не просил!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender2).toFile(),
                link = "https://ru.wikipedia.org/wiki/Шило"
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "Позёры! Я ненавидел Макаревича ещё до того, как это стало модным.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender3).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "Ура! Я богат! И ты тоже… Но это почему-то не радует.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "Интересно. Нет, не то слово. Скучно.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "У меня будет свой луна-парк… С блэкджеком и шлюхами! А хотя к чёрту луна-парк!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender3).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бендер",
                content = "В шахматах нельзя показывать противнику свои козыри.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.bender1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фрай",
                content = "Уф! Какой мне страшный сон приснился! Больше никогда не буду спать!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.fry1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фрай",
                content = "Санта, ты спас мне жизнь. Не убивай меня.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.fry2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фрай",
                content = "Класс! Будущее! Моя семья, коллеги, моя девушка… я их больше никогда не увижу… Ура!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.fry3).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фрай",
                content = "День святого валентина? о, черт…я опять забыл завести девушку.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.fry1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фрай",
                content = "Почему в кепке йогурт? Я могу все объяснить. Он раньше был молоком, но понимаете — время калечит всех…",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.fry2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фарнсворт",
                content = "Я всегда боялся, что он вот так вот убежит. Почему?! Почему… я не сломал ему ноги?!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.farnsworth).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фарнсворт",
                content = "Хорошие новости, народ: по телевизору показывают плохие новости!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.farnsworth2)
                    .toFile(),
                link = "https://yandex.ru/news/"
            )
        )
        saveQuote(
            Quote(
                author = "Фарнсворт",
                content = "Бог мой, надо что-то предпринять… но я уже в пижаме.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.farnsworth).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Фарнсворт",
                content = "Да это же обычная вода… Обычная вода с небольшой примесью ЛСД.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.farnsworth2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бранниган",
                content = "Пока я командую, любая миссия - суицидальная!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.brannigan).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бранниган",
                content = "Гравитация, ты снова победила!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.brannigan).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Бранниган",
                content = "Я получил ваш вызов о помощи и прибыл, как только захотел!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.brannigan).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Гомер Симпсон",
                content = "Как много интересного вы говорите! Как жаль, что это меня мало интересует.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.homer1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Гомер Симпсон",
                content = "Для вранья нужны двое. Один врет, другой слушает.",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.homer2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Гомер Симпсон",
                content = "О, Боже! Космические пришельцы! Не ешьте меня. У меня жена, дети. Съешьте их!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.homer1).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Гомер Симпсон",
                content = "Вообще-то я не религиозный человек, но если ты есть там наверху, спаси меня, Супермен!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.homer2).toFile()
            )
        )
        saveQuote(
            Quote(
                author = "Гомер Симпсон",
                content = "Ты поможешь мне, а я за это приму помощь от тебя!",
                likes = random.nextInt(100) - 5,
                published = 1614766259 - random.nextInt(10000).toLong(),
                imagePath = AppCompatResources.getDrawable(context, R.drawable.homer1).toFile()
            )
        )
    }

}

