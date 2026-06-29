package com.example.dipa_flower.data.api

import com.example.dipa_flower.data.model.News
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.binadesa-tanggapbencana.id/"

    val apiService: ApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(MockInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    private class MockInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val uri = chain.request().url().toString()
            
            // Simulasikan delay jaringan 1 detik agar ProgressBar terlihat
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            if (uri.endsWith("news")) {
                val json = """
                    [
                      {
                        "id": 1,
                        "title": "Gempa Magnitudo 5.2 Guncang Malang, Tidak Berpotensi Tsunami",
                        "image": "https://images.unsplash.com/photo-1541185933-ef5d8ed016c2?w=500",
                        "date": "29 Juni 2026",
                        "location": "Malang, Jawa Timur",
                        "description": "Gempa bumi tektonik mengguncang wilayah Malang Selatan. BMKG menyatakan gempa ini tidak berpotensi tsunami namun warga diimbau tetap waspada terhadap gempa susulan.",
                        "link": "https://www.bmkg.go.id"
                      },
                      {
                        "id": 2,
                        "title": "Banjir Bandang Rendam Puluhan Rumah di Garut",
                        "image": "https://images.unsplash.com/photo-1547683905-f686c993aae5?w=500",
                        "date": "28 Juni 2026",
                        "location": "Garut, Jawa Barat",
                        "description": "Hujan deras selama berjam-jam mengakibatkan sungai Cimanuk meluap dan membanjiri pemukiman warga. Tim SAR dan BPBD sedang melakukan evakuasi korban.",
                        "link": "https://www.bmkg.go.id"
                      },
                      {
                        "id": 3,
                        "title": "Tanah Longsor Tutup Jalur Utama Puncak Bogor",
                        "image": "https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?w=500",
                        "date": "27 Juni 2026",
                        "location": "Bogor, Jawa Barat",
                        "description": "Tebing setinggi 15 meter longsor dan menutupi badan jalan raya Puncak. Petugas gabungan dikerahkan untuk membersihkan material longsor menggunakan alat berat.",
                        "link": "https://www.bmkg.go.id"
                      },
                      {
                        "id": 4,
                        "title": "Peringatan Dini Cuaca Ekstrem dan Hujan Lebat di Jabodetabek",
                        "image": "https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?w=500",
                        "date": "26 Juni 2026",
                        "location": "DKI Jakarta",
                        "description": "BMKG mengeluarkan peringatan dini potensi hujan lebat yang dapat disertai kilat/petir dan angin kencang di sebagian wilayah DKI Jakarta dan sekitarnya.",
                        "link": "https://www.bmkg.go.id"
                      },
                      {
                        "id": 5,
                        "title": "Gunung Merapi Kembali Luncurkan Guguran Awan Panas",
                        "image": "https://images.unsplash.com/photo-1618083707368-b3823daa2726?w=500",
                        "date": "25 Juni 2026",
                        "location": "Sleman, Yogyakarta",
                        "description": "Gunung Merapi meluncurkan awan panas guguran dengan jarak luncur mencapai 1.500 meter ke arah barat daya. Status Merapi berada pada Level III (Siaga).",
                        "link": "https://www.bmkg.go.id"
                      }
                    ]
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), json))
                    .addHeader("content-type", "application/json")
                    .build()
            }

            return Response.Builder()
                .code(404)
                .message("Not Found")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("text/plain"), ""))
                .build()
        }
    }
}
