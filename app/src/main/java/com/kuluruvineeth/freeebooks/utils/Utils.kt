package com.kuluruvineeth.freeebooks.utils

import com.kuluruvineeth.freeebooks.api.models.Author
import java.util.Locale


object Utils {

    fun getAuthorsAsString(authors: List<Author>): String{
        var result: String
        if(authors.size > 1){
            result = authors.first().name
            authors.slice(1 until authors.size).forEach { author ->
                if(author.name != "N/A")
                    result += ", ${author.name}"
            }
        }else{
            result = authors.first().name
        }
        return result
    }

    fun getLanguagesAsString(languages: List<String>): String{
        var result = ""
        languages.forEachIndexed{index, lang ->
            val loc = Locale(lang)
            if(index == 0){
                result = loc.displayLanguage
            }else{
                result += ", ${loc.displayLanguage}"
            }
        }
        return result
    }

    fun getSubjectsAsString(subjects: List<String>, limit:Int): String{

        val allSubjects = ArrayList<String>()
        //strip "--" from subjects
        subjects.forEach { subject ->
            if(subject.contains("--")){
                allSubjects.addAll(subject.split("--"))
            }else{
                allSubjects.add(subject)
            }
        }
        return allSubjects.toSet().joinToString(limit = limit, separator = ", "){
            return@joinToString it.trim()
        }
    }
}