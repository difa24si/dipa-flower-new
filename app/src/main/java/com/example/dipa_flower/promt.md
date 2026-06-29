# 📱 Bina Desa Darurat Bencana

> Aplikasi mobile berbasis Android yang membantu masyarakat memperoleh informasi kebencanaan, melakukan pelaporan, mengakses lokasi evakuasi, serta menerima notifikasi darurat secara real-time.

---

# 📌 Nama Project

**Bina Desa Darurat Bencana**

---

# 📖 Deskripsi Project

Bina Desa Darurat Bencana merupakan aplikasi mobile yang dirancang untuk meningkatkan kesiapsiagaan masyarakat terhadap bencana alam. Aplikasi menyediakan informasi berita kebencanaan terkini, panduan mitigasi, lokasi evakuasi, kontak darurat, serta fitur pelaporan kondisi di lapangan.

Selain memberikan informasi, aplikasi juga membantu masyarakat mengingat jadwal simulasi atau pengecekan perlengkapan darurat melalui sistem reminder.

---

# 🎯 Tujuan Aplikasi

- Menyediakan informasi kebencanaan secara cepat.
- Membantu masyarakat memperoleh berita resmi mengenai bencana.
- Menyediakan edukasi mitigasi bencana.
- Mempermudah pelaporan kondisi darurat.
- Memberikan pengingat kesiapsiagaan bencana.
- Menyimpan data penting secara offline.

---

# 👥 Target Pengguna

- Masyarakat umum
- Relawan
- Aparat desa
- Mahasiswa KKN / Bina Desa
- Tim Penanggulangan Bencana

---

# 🛠 Teknologi

- Kotlin
- Android Studio
- ViewBinding
- RecyclerView
- TabLayout
- ViewPager2
- Retrofit
- Room Database
- Notification Manager
- Alarm Manager / WorkManager
- Material Design 3

---

# 📋 Implementasi Fitur Praktikum

## 1. ListView + Custom Adapter

### Fitur

**Daftar Kontak Darurat**

Menampilkan daftar nomor penting yang dapat dihubungi ketika terjadi bencana.

### Data yang Ditampilkan

- BPBD
- PMI
- Damkar
- Ambulans
- Polisi
- Rumah Sakit
- Posko Desa

### Isi Item

- Icon
- Nama Instansi
- Nomor Telepon
- Tombol Call

### Tujuan

Melatih implementasi ListView menggunakan Custom Adapter.

---

## 2. TabLayout + RecyclerView

Halaman informasi dibagi menjadi beberapa kategori menggunakan TabLayout.

### Tab 1

Berita Bencana

Isi RecyclerView:

- Gempa
- Banjir
- Longsor
- Gunung Meletus
- Tsunami

---

### Tab 2

Panduan Mitigasi

Isi RecyclerView:

- Sebelum Gempa
- Saat Gempa
- Setelah Gempa
- Evakuasi Banjir
- Pertolongan Pertama

---

### Tab 3

Lokasi Evakuasi

Isi RecyclerView:

- Nama Posko
- Alamat
- Kapasitas
- Status

---

### Tujuan

Mengimplementasikan TabLayout dan RecyclerView dalam satu halaman.

---

WARNA SESUAIKAN DENGAN UI PROJECT SEKARANG
---


## 3. Onboarding Screen (ViewPager2)

Saat aplikasi pertama kali dibuka, tampilkan onboarding sebanyak 3 halaman.

### Halaman 1

Judul

**Selamat Datang**

Deskripsi

Aplikasi informasi dan kesiapsiagaan bencana untuk masyarakat.

---

### Halaman 2

Judul

**Pantau Informasi Resmi**

Deskripsi

Dapatkan berita kebencanaan terbaru dari sumber terpercaya.

---

### Halaman 3

Judul

**Siap Menghadapi Bencana**

Deskripsi

Kelola informasi, pelajari mitigasi, dan aktifkan pengingat kesiapsiagaan.

---

### Komponen

- ViewPager2
- Indicator
- Skip
- Next
- Mulai

---

## 4. REST API (Berita Darurat Alam)

Menggunakan Retrofit untuk mengambil berita bencana dari API.

### Data Ditampilkan

- Judul Berita
- Gambar
- Tanggal
- Lokasi
- Deskripsi Singkat
- Link Berita

### Contoh Kategori

- Gempa
- Banjir
- Longsor
- Cuaca Ekstrem
- Gunung Meletus

### Implementasi

- Retrofit
- Gson Converter
- RecyclerView
- ProgressBar
- Error Handling

---

## 5. Room Database

Digunakan untuk menyimpan data secara offline.

### Tabel

#### Favorite News

Field

- id
- title
- image
- location
- date
- description

Fungsi

Menyimpan berita penting agar tetap bisa dibaca tanpa internet.

---

#### Emergency Checklist

Field

- id
- item
- completed

Contoh Data

- Air Minum
- Senter
- Power Bank
- Obat-obatan
- Dokumen Penting
- Makanan Siap Saji
- Peluit

Fungsi

Checklist perlengkapan darurat.

---

## 6. Notification & Reminder

Menggunakan NotificationManager dan AlarmManager/WorkManager.

### Reminder Harian

Notifikasi:

> Jangan lupa periksa perlengkapan darurat Anda hari ini.

---

### Reminder Mingguan

Notifikasi:

> Saatnya melakukan simulasi kesiapsiagaan bencana bersama keluarga.

---

### Reminder Berita Baru

Jika terdapat berita baru dari API:

> Ada informasi bencana terbaru di wilayah Indonesia.

---

# 📱 Struktur Menu Aplikasi

## Home

- Ringkasan Informasi
- Berita Terbaru
- Tombol Akses Cepat

---

## Berita

Menampilkan berita bencana dari REST API.

---

## Mitigasi

Berisi panduan menghadapi berbagai jenis bencana.

---

## Evakuasi

Menampilkan daftar lokasi evakuasi dan posko darurat.

---

## Checklist

Checklist perlengkapan darurat yang tersimpan di Room Database.

---

## Kontak Darurat

Daftar nomor penting menggunakan ListView + Custom Adapter.

---

## Profil

- Tentang Aplikasi
- Versi
- Pengembang

---

# 🗂 Struktur Fitur Berdasarkan Praktikum

| No | Materi Praktikum | Implementasi |
|----|------------------|--------------|
| 1 | ListView + Custom Adapter | Daftar Kontak Darurat |
| 2 | TabLayout + RecyclerView | Berita, Mitigasi, Evakuasi |
| 3 | ViewPager2 | Onboarding Screen |
| 4 | REST API | Berita Darurat Alam |
| 5 | Room Database | Favorite News & Checklist Darurat |
| 6 | Notification & Reminder | Pengingat Checklist, Simulasi, dan Berita Baru |

---

# 🚀 Hasil Akhir

Aplikasi **Bina Desa Darurat Bencana** mengintegrasikan seluruh materi praktikum Android ke dalam satu studi kasus yang relevan dengan mitigasi bencana. Pengguna dapat memperoleh informasi resmi melalui REST API, menyimpan data penting secara offline menggunakan Room Database, menerima notifikasi pengingat, melihat konten berdasarkan kategori melalui TabLayout dan RecyclerView, mengakses kontak darurat menggunakan ListView dengan Custom Adapter, serta memperoleh pengalaman awal yang lebih baik melalui Onboarding Screen berbasis ViewPager2.