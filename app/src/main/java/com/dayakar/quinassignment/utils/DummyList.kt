package com.dayakar.quinassignment.utils

import com.dayakar.quinassignment.model.Story

/*
* Created By DAYAKAR GOUD BANDARI on 03-04-2021.
*/


//this is a dummy list for testing

fun getDummyStories():List<Story>{

    return mutableListOf<Story>(
        Story(1,"Akhil Cherala","akhil@email.com","Coimbatore",
        "https://images.pexels.com/photos/45987/pexels-photo-45987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "Reached at the top",System.currentTimeMillis()),

        Story(2,"Dayakar Goud","dayakar@gmail.com","Hyderabad",
            "https://s3.ap-southeast-1.amazonaws.com/images.deccanchronicle.com/dc-Cover-fohvsrnbbi7rkkntepci5honc1-20200316132831.Medi.jpeg",
            "Enjoying at wonderlla",System.currentTimeMillis()),

        Story(3,"Aravind ","arav@email.com","Pune",
            "https://i.pinimg.com/originals/f3/18/e7/f318e7d7cb2f475171e1ad37924f723d.jpg",
            "Don't know what's going on",System.currentTimeMillis()),

        Story(4,"Revanth Rev","revanth@email.com","Kanyakumari",
            "https://imgcld.yatra.com/ytimages/image/upload/t_yt_blog_w_800_c_fill_g_auto_q_auto:good_f_jpg/v1527054549/Kanyakumari_Blog0_1527054545.jpg",
            "Sunset at the end....",System.currentTimeMillis()),

        Story(5,"Adhithya","adithya@hotmail.com","Warangal",
            "https://food-images.files.bbci.co.uk/food/recipes/how_to_make_pizza_50967_16x9.jpg",
            "Awesome food ",System.currentTimeMillis()),

        Story(6,"Gourav k ","gourav@email.com","Gourav",
            "https://www.fabhotels.com/blog/wp-content/uploads/2019/05/Marina-Beach.jpg",
            "Beach sand and more",System.currentTimeMillis()),

        Story(7,"Sai ram","sairam@email.com","Kolkata",
            "https://static.toiimg.com/photo/62804666/.jpg",
            "No words to describe this... ",System.currentTimeMillis()),

        Story(8,"Mahesh kumar","mahesh@email.com","Ooty",
"https://img.traveltriangle.com/blog/wp-content/uploads/2020/01/Ooty-In-Summer-cover_17th-Jan.jpg",
            "Definetly i will come again",System.currentTimeMillis()),
    )



}