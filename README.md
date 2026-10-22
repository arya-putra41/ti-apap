# SIMBG: Sistem Informasi Makan Bergizi Gratis - Modul 2: Sekolah dan Penerima Manfaat

dikerjakan oleh:

Arya Putra Parikesit

NPM 2406406300

## Penjelasan Umum
SIMBG merupakan sistem informasi yang menunjang pelaksanaan program Makan Bergizi Gratis (MBG) oleh pemerintah Republik Indonesia. Sistem ini bertujuan untuk menghubungkan berbagai aktor dalam proses bisnis MBG, seperti siswa, staf sekolah, staf dapur, ahli gizi, dan petugas MBG.

Modul ini berfokus kepada penerima manfaat MBG, yaitu siswa. Untuk itu, modul ini juga mencatat data sekolah dan rombongan belajar (rombel) yang menampung siswa tersebut. Dengan modul ini, petugas MBG dapat mengelola data sekolah dan data rombel serta memproses pengajuan penerimaan MBG oleh siswa.

Mekanisme utama jalannya modul ini adalah seperti berikut:
1. **Sekolah** mitra (berstatus aktif) memiliki satu atau lebih **rombongan belajar**, masing-masing dengan kuota penerima tertentu.
2. Setiap siswa yang didata dicatat sebagai **penerima manfaat** baru dengan status "Diajukan". Begitu didata, siswa tersebut akan mengambil 1 kursi di kuota rombel. 
3. **Petugas verifikasi** memeriksa pengajuan penerima untuk memastikan kelengkapan data. Jika sudah dianggap lengkap, petugas akan memverifikasi oengajuan sehingga statusnya berubah menjadi "Terverifikasi"
4. Pengajuan yang terverifikasi kemudian dapat diaktifkan (status menjadi "Aktif"), menandai bahwa siswa sudah siap menerima MBG.
5. Jika pada tahap "Diajukan" atau "Terverifikasi" pengajuan dianggap tidak layak, maka status akan berubah menjadi "Ditolak". Kursi yang diambil siswa di rombel tersebut akan terbuka kembali.
6. Jika setelah aktivasi, siswa sudah tidak menerima MBG lagi (misalnya karena sudah lulus), maka pengajuan akan berubah menjadi "Nonaktif". Kursi yang diambil siswa di rombel tersebut akan terbuka kembali.
7. Selama belum final, pengajuan dapat dikembalikan ke status "Diajukan" lagi untuk meminta (dari "Terverifikasi") atau menanggapi (dari "Aktif") perubahan biodata.
8. Setiap perubahan status pengajuan di atas akan menyimpan **riwayat perubahan status** baru untuk kepentingan audit.

Modul ini juga memiliki dua mekanisme otomatis, yaitu penolakan pengajuan yang kedaluwarsa dan penonaktifan massal siswa pada rombel yang ditutup.
- Jika sebuah pengajuan berada di status "Diajukan" selama 14 hari berturut-turut tanpa diverifikasi, maka sistem akan otomatis menolak pengajuan tersebut. Pengecekan pengajuan yang kedaluwarsa dilakukan secara berkala.
- Apabila sebuah rombongan belajar ditutup, maka sistem akan otomatis menonaktifkan semua pengajuan yang masih berjalan dari rombel tersebut.

## Fitur Modul 2
### Home Page
URL frontend: /

URL backend: GET /api/home

Menampilkan halaman utama modul. Halaman ini memiliki dua *point of interest*: navbar dan data statistik modul. Navbar diimplementasikan di setiap halaman melalui App.vue, sedangkan data statistik modul diperoleh dari backend melalui API. Informasi yang ditampilkan adalah:
- Jumlah sekolah berstatus Aktif
- Jumlah rombel berstatus Aktif
- Jumlah penerima berstatus Aktif
- Persentase kuota penerima yang sudah terpakai (dihitung dengan logic: total jumlah penerima terdaftar di semua rombel / total kuota penerima di semua rombel)
- Jumlah penerima yang berstatus Diajukan dan masih menunggu verifikasi


### Sekolah
#### Homepage Sekolah
URL frontend: /sekolah

URL backend: GET /api/sekolah

Mengembalikan daftar sekolah yang saat ini terdaftar di SIMBG. Daftar sekolah dapat di-*search* dan difilter berdasarkan jenjang. Informasi yang ditampilkan:
- ID sekolah
- NPSN (Nomor Pokok Sekolah Nasional)
- Nama sekolah
- Jenjang (SD, SMP, SMA/SMK)
- Status mitra (Aktif atau Nonaktif)

#### Detail Sekolah
URL frontend: /sekolah/{id}

URL backend: GET /api/sekolah/{id}

Mengembalikan informasi terperinci mengenai sekolah. Informasi yang ditampilkan:
- ID sekolah
- NPSN (Nomor Pokok Sekolah Nasional)
- Nama sekolah
- Jenjang (SD, SMP, SMA/SMK)
- Status mitra (Aktif atau Nonaktif)
- Lokasi (terdiri atas provinsi, kabupaten/kota, kecamatan, desa/kelurahan, dan jalan)
- Nama kepala sekolah
- Tanggal sekolah bergabung sebagai mitra MBG
- Nomor telepon sekolah

Selain data sekolah, halaman ini juga menampilkan daftar rombongan belajar yang dimiliki sekolah. Daftar rombel dapat di-*search*. Informasi yang ditampilkan untuk setiap rombongan belajar:
- Nama rombel
- Tahun ajaran
- Tingkat (kelas 1 - 12)
- Kuota penerima rombel
- Jumlah siswa yang saat ini tercatat pada rombel
- Status rombel (Aktif atau Ditutup)

#### Create Sekolah
URL frontend: /sekolah/create

URL backend: POST /api/sekolah/create

Endpoint untuk mendaftarkan data sekolah baru. Data yang perlu diinput adalah:
- NPSN (8 digit)
- Nama sekolah
- Jenjang
- Lokasi (terdiri atas provinsi, kabupaten/kota, kecamatan, desa/kelurahan, dan jalan)
- Nama kepala sekolah
- Tanggal sekolah bergabung sebagai mitra MBG (harus sebelum sekarang)
- Nomor telepon sekolah

Data lokasi (kecuali jalan) diperoleh dari sumber eksternal, yaitu API wilayah.id. Isian pengguna dikirimkan ke backend melalui objek DTO CreateSekolahRequest.

#### Update Sekolah
URL frontend: /sekolah/{id}/update

URL backend: PUT /api/sekolah/update

Endpoint untuk mengubah data sekolah. Di awal, kolom-kolom akan otomatis terisi dengan data sekolah yang dipilih. Data yang perlu diinput adalah:
- Nama sekolah
- Jenjang
- Lokasi (terdiri atas provinsi, kabupaten/kota, kecamatan, desa/kelurahan, dan jalan)
- Nama kepala sekolah
- Tanggal sekolah bergabung sebagai mitra MBG (harus sebelum sekarang)
- Nomor telepon sekolah

Data lokasi (kecuali jalan) diperoleh dari sumber eksternal, yaitu API wilayah.id. Isian pengguna dikirimkan ke backend melalui objek DTO UpdateSekolahRequest.

#### Nonaktifkan Sekolah
URL frontend: -

URL backend: DELETE /api/sekolah/{id}/nonaktifkan

Tidak memiliki halaman khusus di frontend karena diakses melalui *modal window* di halaman detail sekolah. Sekolah hanya dapat dinonaktifkan jika semua rombel yang dimilikinya sudah berstatus "Ditutup"; sistem akan menolak penonaktifan dan mengeluarkan pesan error jika ketentuan ini dilanggar.

#### Cari Sekolah Aktif
URL frontend: -

URL backend: GET /api/sekolah/aktif

Tidak dapat diakses langsung di frontend; digunakan sebagai API untuk memperoleh data sekolah aktif yang diperlukan ketika mendaftarkan rombel.


### Rombongan Belajar (Rombel)
#### Homepage Rombel
URL frontend: /rombel

URL backend: GET /api/rombel

Mengembalikan daftar rombel yang saat ini terdaftar di SIMBG. Daftar rombel dapat di-*search* dan difilter berdasarkan status. Informasi yang ditampilkan:
- ID rombel
- Nama sekolah pemilik rombel
- Nama rombel
- Tahun ajaran
- Tingkat (kelas 1 - 12)
- Kuota penerima rombel
- Jumlah siswa yang saat ini tercatat pada rombel
- Kuota rombel yang masih tersisa   
- Status rombel (Aktif atau Ditutup)

#### Detail Rombel
URL frontend: /rombel/{id}

URL backend: GET /api/rombel/{id}

Menampilkan informasi rinci mengenai rombongan belajar. Informasi yang ditampilkan:
- ID rombel
- Nama sekolah pemilik rombel
- Nama rombel
- Tahun ajaran
- Tingkat (kelas 1 - 12)
- Kuota penerima rombel
- Jumlah siswa yang saat ini tercatat pada rombel
- Kuota rombel yang masih tersisa   
- Status rombel (Aktif atau Ditutup)

Selain data rombel, halaman ini juga menampilkan daftar siswa (penerima manfaat) yang terdaftar di rombel tersebut. Daftar penerima dapat di-*search*. Informasi yang ditampilkan untuk masing-masing penerima:
- ID penerima
- Nama penerima
- NISN (Nomor Induk Siswa Nasional)
- Kondisi khusus yang dimiliki (tidak ada, alergi makanan, diet khusus, disabilitas, atau lainnya)
- Tanggal pengajuan penerima tersebut dibuat
- Status penerima (Diajukan, Terverifikasi, Aktif, Ditolak, atau Nonaktif)

#### Create Rombel
URL frontend: /rombel/create

URL backend: POST /api/rombel/create

Endpoint untuk mendaftarkan data rombel baru. Data yang perlu diinput adalah:
- Sekolah pemilik rombel (harus sekolah berstatus Aktif)
- Nama rombel
- Tingkat (kelas 1 sampai 12)
- Tahun ajaran
- Kuota penerima (harus lebih dari 0)

Isian pengguna dikirimkan ke backend melalui objek DTO CreateRombelRequest.

Perlu diketahui bahwa halaman ini dapat diakses dari dua tempat, yaitu halaman homepage rombel dan halaman detail sekolah. Jika dibuka dari halaman detail sekolah, kolom sekolah pemilik rombel akan otomatis diisi dengan sekolah yang bersangkutan.

#### Update Rombel
URL frontend: /rombel/{id}/update

URL backend: PUT /api/rombel/update

Endpoint untuk mengubah data rombel. Di awal, kolom-kolom akan otomatis terisi dengan data rombel yang dipilih. Data yang perlu diinput adalah:
- Kuota penerima (harus lebih dari 0)
- Status (ubah "Aktif" menjadi "Ditutup")

Isian pengguna dikirimkan ke backend melalui objek DTO UpdateRombelRequest. Jika pengguna menutup rombel, maka seluruh penerima manfaat di rombel tersebut akan dinonaktifkan. Rombel yang sudah dinonaktifkan tidak dapat diaktifkan kembali.

#### Cari Rombel Aktif
URL frontend: -

URL backend: GET /api/rombel/aktif

Tidak dapat diakses langsung di frontend; digunakan sebagai API untuk memperoleh data rombel aktif yang diperlukan ketika mendaftarkan penerima manfaat.


### Penerima Manfaat
#### Homepage Penerima
URL frontend: /penerima

URL backend: GET /api/penerima

Mengembalikan daftar penerima manfaat yang saat ini terdaftar di SIMBG. Daftar dapat di-*search* dan difilter berdasarkan status. Data yang ditampilkan:
- ID penerima
- Nama penerima
- NISN (Nomor Induk Siswa Nasional)
- Rombongan belajar dan asal sekolah
- Kondisi khusus (tidak ada, alergi makanan, diet khusus, disabilitas, atau lainnya)
- Status penerima (Diajukan, Terverifikasi, Aktif, Ditolak, atau Nonaktif)

#### Detail Penerima
URL frontend: /penerima/{id}

URL backend: GET /api/penerima/{id}

Menampilkan informasi rinci mengenai penerima manfaat. Data yang ditampilkan:
- ID penerima
- Nama penerima
- NISN (Nomor Induk Siswa Nasional)
- Jenis kelamin penerima
- Tanggal lahir penerima
- Rombongan belajar dan asal sekolah
- Kondisi khusus (tidak ada, alergi makanan, diet khusus, disabilitas, atau lainnya)
- Status penerima (Diajukan, Terverifikasi, Aktif, Ditolak, atau Nonaktif)
- Tanggal dan waktu pengajuan untuk penerima tersebut dibuat
- Tanggal dan waktu penerima tersebut diverifikasi, jika ada
- Petugas verifikasi yang memverifikasi penerima, jika ada
- Tanggal dan waktu penerima tersebut diaktifkan, jika ada

Selain data penerima, halaman ini juga menampilkan daftar riwayat perubahan status penerima. Untuk setiap perubahan status, data yang ditampilkan:
- Tanggal dan waktu perubahan terjadi
- Perubahan status (status sebelumnya dan status setelahnya)
- Keterangan perubahan

#### Create Penerima
URL frontend: /penerima/create

URL backend: POST /api/penerima/create

Endpoint untuk mendaftarkan data penerima baru. Data yang perlu diinput:
- Rombongan belajar pemilik penerima
- NISN (10 digit)
- Nama penerima
- Jenis kelamin (laki-laki atau perempuan)
- Tanggal lahir (harus sebelum sekarang)
- Kondisi khusus
- Keterangan (wajib diisi jika penerima memiliki kondisi khusus)

Isian data dikirim ke backend melalui objek DTO CreatePenerimaRequest.

Perlu diketahui bahwa halaman ini dapat diakses dari dua tempat, yaitu dari halaman homepage penerima dan dari halaman detail rombel. Jika halaman dibuka dari detail rombel, maka kolom rombel pemilik penerima akan otomatis diisi dengan rombel yang bersangkutan.

#### Update Penerima
URL frontend: /penerima/{id}/update

URL backend: PUT /api/penerima/update

Endpoint untuk mengubah data penerima. Di awal, kolom-kolom akan otomatis terisi dengan data penerima yang dipilih. Data yang perlu diinput:
- Nama penerima
- Jenis kelamin (laki-laki atau perempuan)
- Tanggal lahir (harus sebelum sekarang)
- Kondisi khusus
- Keterangan (wajib diisi jika penerima memiliki kondisi khusus)

Isian data dikirim ke backend melalui objek DTO UpdatePenerimaRequest.

Apabila data penerima diubah ketika penerima dalam status Aktif, maka penerima akan dikembalikan ke status Diajukan dan riwayat perubahan dicatat. Penerima berstatus Ditolak atau Nonaktif tidak dapat diubah datanya.

#### Verifikasi Penerima
URL frontend: /penerima/{id}/verifikasi

URL backend: PUT /api/penerima/verifikasi

Endpoint untuk memverifikasi penerima. Hanya penerima berstatus Diajukan yang dapat diverifikasi. Data yang perlu diinput:
- Petugas verifikasi yang bertanggung jawab atas penerima
- Catatan verifikasi

Isian data dikirim ke backend melalui objek DTO VerifyPenerimaRequest. Status penerima berubah menjadi Terverifikasi.

#### Aktivasi Penerima
URL frontend: -

URL backend: PUT /api/penerima/{id}/aktivasi

Tidak memiliki halaman khusus di frontend karena diakses melalui *modal window* di halaman detail penerima. Hanya penerima berstatus Terverifikasi yang dapat diaktifkan. Status penerima berubah menjadi Aktif.

#### Tolak Penerima
URL frontend: /penerima/{id}/tolak

URL backend: PUT /api/penerima/tolak

Endpoint untuk menolak penerima. Hanya penerima berstatus Diajukan atau Terverifikasi yang dapat ditolak. Data yang perlu diinput:
- Alasan penolakan

Isian data dikirim ke backend melalui objek DTO RejectPenerimaRequest. Status penerima berubah menjadi Ditolak.

Logika ini terpisah dari logika penolakan otomatis akibat pengajuan kedaluwarsa.

#### Nonaktifkan Penerima
URL frontend: /penerima/{id}/nonaktifkan

URL backend: PUT /api/penerima/nonaktifkan

Endpoint untuk menonaktifkan penerima. Hanya penerima berstatus Aktif yang dapat dinonaktifkan. Data yang perlu diinput:
- Alasan penonaktifan

Isian data dikirim ke backend melalui objek DTO DeactivatePenerimaRequest. Status penerima berubah menjadi Nonaktif.

Logika ini terpisah dari logika penonaktifan otomatis akibat penutupan rombel pemilik.

#### Tinjau Ulang Penerima
URL frontend: /penerima/{id}/tinjau-ulang

URL backend: PUT /api/penerima/tinjau-ulang

Endpoint untuk meninjau ulang penerima. Hanya penerima berstatus Terverifikasi yang dapat ditinjau ulang. Data yang perlu diinput:
- Alasan peninjauan ulang

Isian data dikirim ke backend melalui objek DTO ReviewPenerimaRequest. Status penerima berubah menjadi Diajukan.

#### Statistik Penerima
URL frontend: /penerima/statistik

URL backend: GET /api/penerima/statistik?period={period}&year={year}

Menampilkan statistik pengajuan, verifikasi, dan penolakan penerima. Statistik yang ditampilkan adalah rentang waktu satu tahun, dikelompokkan berdasarkan bulan atau kuartal sesuai pilihan pengguna. Statistik ditampilkan dalam dua bentuk, yaitu diagram batang dan tabel.

#### Antrian Verifikasi Penerima
URL frontend: /penerima/antrian-verifikasi

URL backend: GET /api/penerima/antrian-verifikasi

Menampilkan penerima manfaat berstatus Diajukan yang masih menunggu verifikasi. Daftar pengajuan diurutkan dari yang tertua dahulu. Pengajuan yang sudah mendekati batas 14 hari tanpa verifikasi ditampilkan dengan warna yang mencolok untuk menarik perhatian pengguna.


### Petugas Verifikasi
#### Cari Semua Petugas Verifikasi
URL frontend: -

URL backend: /api/petugas

Tidak dapat diakses langsung di frontend; digunakan sebagai API untuk memperoleh data petugas yang diperlukan saat memverifikasi pengajuan penerima manfaat.


### Wilayah
#### Cari Provinsi
URL frontend: -

URL backend: /api/wilayah/provinsi

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencari data provinsi (province).

#### Cari Kabupaten/Kota
URL frontend: -

URL backend: /api/wilayah/kota/{provinceCode}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencari data kabupaten/kota (regency) di provinsi tertentu.

#### Cari Kecamatan
URL frontend: -

URL backend: /api/wilayah/kecamatan/{regencyCode}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencari data kecamatan (district) di kabupaten/kota tertentu.

#### Cari Desa/Kelurahan
URL frontend: -

URL backend: /api/wilayah/kelurahan/{districtCode}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencari data desa/kelurahan (village) di kecamatan tertentu.

#### Cari Provinsi Berdasarkan Nama
URL frontend: -

URL backend: /api/wilayah/provinsi/code?name={name}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencocokkan nama provinsi dengan kodenya (untuk kepentingan edit sekolah)

#### Cari Kabupaten/Kota Berdasarkan Nama
URL frontend: -

URL backend: /api/wilayah/kota/code?name={name}&provinceCode={provinceCode}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencocokkan nama kabupaten/kota di provinsi tertentu dengan kodenya (untuk kepentingan edit sekolah)

#### Cari Kecamatan Berdasarkan Nama
URL frontend: -

URL backend: /api/wilayah/kecamatan/code?name={name}&regencyCode={regencyCode}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencocokkan nama kecamatan di kabupaten/kota tertentu dengan kodenya (untuk kepentingan edit sekolah)

#### Cari Kelurahan Berdasarkan Nama
URL frontend: -

URL backend: /api/wilayah/kelurahan/code?name={name}&districtCode={districtCode}

Tidak dapat diakses langsung di frontend. Digunakan untuk berkomunikasi dengan API wilayah.id untuk mencocokkan nama desa/kelurahan di kecamatan tertentu dengan kodenya (untuk kepentingan edit sekolah)


## Penjelasan Struktur Proyek
### simbg-2406406300-be
Menyimpan kode backend proyek yang berdasarkan Java dengan framework Spring Boot. Kode utama proyek terletak di folder src/main/java/apap/ti/_6/simbg_2406406300_be.
- configs: berisi kelas konfigurasi yang dapat digunakan untuk mengubah *behavior* sistem, tepatnya terkait dengan jumlah hari sebelum penolakan otomatis.
- controller: berisi kelas controller yang berfungsi menangkap HTTP *request* dari frontend, meneruskannya ke *service layer*, dan mengembalikan hasilnya dalam bentuk HTTP *response*.
- dto: berisi DTO (*data transfer object*) yang berfungsi membungkus informasi dari backend menjadi bentuk yang mudah dicerna, atau menambahkan informasi lain yang perlu didapatkan dari *service layer*.
- exceptions: berisi objek *exception* yang berfungsi menangani error yang terjadi dalam operasional sistem.
- httpresponses: berisi *wrapper* untuk HTTP *response* yang membantu mengenkapsulasi data dan meneruskan informasi seperti kode HTTP dan *timestamp*.
- models: berisi model data yang mewakili entitas-entitas dalam sistem. Setiap model menyimpan data dan juga memiliki method untuk melakukan operasi-operasi tertentu yang bisa dijalankannya secara independen (misalnya pergantian status atau penambahan jumlah siswa).
- repo: berisi *repository* (*interface* penyimpanan data) yang menggunakan JPA untuk mengakses basis data PostgreSQL di Docker.
- services: berisi *service layer* sistem yang bertugas menjalankan transaksi terhadap objek, menghubungkan objek dengan objek lain atau bahkan kelas lain, dan mengakses basis data melalui *repository*.
- utils: berisi komponen sistem yang bersifat *miscellaneous*.

#### Services
- home
    - HomeService: berfungsi mengambil data statistik modul dengan mengumpulkan data dari sekolah, rombel, dan penerima.
- penerima
    - PenerimaService: *service* utama untuk penerima dengan fungsi pencarian, create, update, dan perubahan status.
    - PenerimaQueryService: berfungsi hanya untuk mengambil data penerima dari basis data.
    - PenerimaDeactivatorService: berfungsi untuk menonaktifkan penerima pada rombongan belajar yang ditutup.
    - PenerimaAutoOperationsService: berfungsi untuk mengecek kedaluwarsanya pengajuan penerima dan menolak pengajuan jika sudah melewati tenggat waktu.
    - PenerimaStatisticsService: berfungsi untuk mengambil statistik penerima.
- petugas
    - PetugasQueryService: berfungsi mengambil data petugas dari basis data.
- riwayat
    - RiwayatService: *service* utama untuk riwayat perubahan status dengan fungsi pencarian dan create. Riwayat bersifat immutable setelah dicatat.
- rombel
    - RombelService: *service* utama untuk rombel, dengan fitur pencarian, create, dan update.
    - RombelQueryService: berfungsi hanya untuk mengambil data rombel dari basis data.
- sekolah
    - SekolahService: *service* utama untuk sekolah, dengan fitur pencarian, create, dan update.
    - SekolahQueryService: berfungsi hanya untuk mengambil data sekolah dari basis data.
- wilayahid
    - WilayahIdService: berfungsi untuk mengakses dan mengambil data dari API wilayah.id.

#### utils
- AutoRejecter: berfungsi untuk menjalankan operasi terjadwal untuk mengecek pengajuan kedaluwarsa.
- IdGeneratorService: berfungsi untuk menciptakan ID sekuensial untuk sekolah, rombel, penerima, petugas verifikasi, dan riwayat perubahan status.
- Seeder: berfungsi untuk menciptakan data *dummy* untuk kepentingan testing.


### simbg-2406406300-fe
Menyimpan kode frontend proyek yang berdasarkan Vue. Kode utama proyek terletak di folder /src/components.
- components
    - common: berisi elemen-elemen serbaguna yang digunakan di banyak view.
    - forms: berisi komponen *web form* yang ditampilkan ke pengguna untuk keperluan pengisian data.
    - layout: berisi komponen dasar aplikasi yang ditampilkan pada semua halaman, seperti navbar.
- router: berisi router yang bertugas untuk menangkap HTTP *response* dan memilih view yang harus ditampilkan untuk menanggapi *response* tersebut.
- services: berisi API yang digunakan untuk mengakses backend serta representasi data yang akan dikirim ke dan diperoleh dari backend.
- utils: berisi komponen frontend yang bersifat *miscellaneous*.
- views: berisi kode utama untuk tampilan aplikasi yang akan disajikan ke pengguna, berupa kombinasi script TypeScript, layout HTML, dan styling Tailwind.