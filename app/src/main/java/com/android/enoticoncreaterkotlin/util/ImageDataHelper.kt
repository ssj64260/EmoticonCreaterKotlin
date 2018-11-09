package com.android.enoticoncreaterkotlin.util

import com.android.enoticoncreaterkotlin.R

class ImageDataHelper {

    companion object {
        val SECRET_LIST = intArrayOf(
                R.raw.img_secret1, R.raw.img_secret2, R.raw.img_secret3,
                R.raw.img_secret4, R.raw.img_secret5, R.raw.img_secret6,
                R.raw.img_secret7, R.raw.img_secret8, R.raw.img_secret9,
                R.raw.img_secret10)

        val SECRET_TITLES = arrayOf(
                "福尔泰 + 乌蝇哥", "教皇 + 乌蝇哥", "金馆长 + 乌蝇哥",
                "福尔泰 + 教皇", "教皇 + 教皇", "金馆长 + 教皇",
                "福尔泰 + 教皇", "教皇 + 教皇", "金馆长 + 教皇",
                "教皇 + 教皇")

        val EMOTICON_TITLES = arrayOf("蘑菇头", "熊猫人", "滑稽")

        val MO_GU_TOU_LIST = intArrayOf(
                R.raw.img_mogutou1, R.raw.img_mogutou2, R.raw.img_mogutou3,
                R.raw.img_mogutou4, R.raw.img_mogutou5, R.raw.img_mogutou6,
                R.raw.img_mogutou7, R.raw.img_mogutou8, R.raw.img_mogutou9,
                R.raw.img_mogutou10, R.raw.img_mogutou11, R.raw.img_mogutou12,
                R.raw.img_mogutou13, R.raw.img_mogutou14, R.raw.img_mogutou15,
                R.raw.img_mogutou16, R.raw.img_mogutou17, R.raw.img_mogutou18,
                R.raw.img_mogutou19, R.raw.img_mogutou20, R.raw.img_mogutou21,
                R.raw.img_mogutou22, R.raw.img_mogutou23, R.raw.img_mogutou24,
                R.raw.img_mogutou25, R.raw.img_mogutou26, R.raw.img_mogutou27,
                R.raw.img_mogutou28, R.raw.img_mogutou29, R.raw.img_mogutou30,
                R.raw.img_mogutou31, R.raw.img_mogutou32, R.raw.img_mogutou33,
                R.raw.img_mogutou34, R.raw.img_mogutou35, R.raw.img_mogutou36,
                R.raw.img_mogutou37, R.raw.img_mogutou38, R.raw.img_mogutou39,
                R.raw.img_mogutou40, R.raw.img_mogutou41, R.raw.img_mogutou42,
                R.raw.img_mogutou43, R.raw.img_mogutou44, R.raw.img_mogutou45,
                R.raw.img_mogutou46, R.raw.img_mogutou47, R.raw.img_mogutou48,
                R.raw.img_mogutou49, R.raw.img_mogutou50, R.raw.img_mogutou51,
                R.raw.img_mogutou52, R.raw.img_mogutou53, R.raw.img_mogutou54,
                R.raw.img_mogutou55, R.raw.img_mogutou56, R.raw.img_mogutou57,
                R.raw.img_mogutou58, R.raw.img_mogutou59, R.raw.img_mogutou60,
                R.raw.img_mogutou61, R.raw.img_mogutou62, R.raw.img_mogutou63,
                R.raw.img_mogutou64, R.raw.img_mogutou65, R.raw.img_mogutou66,
                R.raw.img_mogutou67, R.raw.img_mogutou68, R.raw.img_mogutou69,
                R.raw.img_mogutou70, R.raw.img_mogutou71, R.raw.img_mogutou72,
                R.raw.img_mogutou73, R.raw.img_mogutou74, R.raw.img_mogutou75,
                R.raw.img_mogutou76, R.raw.img_mogutou77, R.raw.img_mogutou78,
                R.raw.img_mogutou79, R.raw.img_mogutou80, R.raw.img_mogutou81,
                R.raw.img_mogutou82, R.raw.img_mogutou83, R.raw.img_mogutou84,
                R.raw.img_mogutou85, R.raw.img_mogutou86, R.raw.img_mogutou87,
                R.raw.img_mogutou88, R.raw.img_mogutou89, R.raw.img_mogutou90)
        val XIONG_MAO_REN_LIST = intArrayOf(
                R.raw.img_xiongmaoren1, R.raw.img_xiongmaoren2, R.raw.img_xiongmaoren3,
                R.raw.img_xiongmaoren4, R.raw.img_xiongmaoren5, R.raw.img_xiongmaoren6,
                R.raw.img_xiongmaoren7, R.raw.img_xiongmaoren8, R.raw.img_xiongmaoren9,
                R.raw.img_xiongmaoren10, R.raw.img_xiongmaoren11, R.raw.img_xiongmaoren12,
                R.raw.img_xiongmaoren13, R.raw.img_xiongmaoren14, R.raw.img_xiongmaoren15,
                R.raw.img_xiongmaoren16, R.raw.img_xiongmaoren17, R.raw.img_xiongmaoren18,
                R.raw.img_xiongmaoren19, R.raw.img_xiongmaoren20, R.raw.img_xiongmaoren21,
                R.raw.img_xiongmaoren22, R.raw.img_xiongmaoren23, R.raw.img_xiongmaoren24,
                R.raw.img_xiongmaoren25, R.raw.img_xiongmaoren26, R.raw.img_xiongmaoren27,
                R.raw.img_xiongmaoren28, R.raw.img_xiongmaoren29, R.raw.img_xiongmaoren30,
                R.raw.img_xiongmaoren31, R.raw.img_xiongmaoren32, R.raw.img_xiongmaoren33,
                R.raw.img_xiongmaoren34, R.raw.img_xiongmaoren35, R.raw.img_xiongmaoren36,
                R.raw.img_xiongmaoren37, R.raw.img_xiongmaoren38, R.raw.img_xiongmaoren39,
                R.raw.img_xiongmaoren40, R.raw.img_xiongmaoren41, R.raw.img_xiongmaoren42,
                R.raw.img_xiongmaoren43, R.raw.img_xiongmaoren44, R.raw.img_xiongmaoren45,
                R.raw.img_xiongmaoren46, R.raw.img_xiongmaoren47, R.raw.img_xiongmaoren48,
                R.raw.img_xiongmaoren49, R.raw.img_xiongmaoren50, R.raw.img_xiongmaoren51,
                R.raw.img_xiongmaoren52, R.raw.img_xiongmaoren53, R.raw.img_xiongmaoren54,
                R.raw.img_xiongmaoren55, R.raw.img_xiongmaoren56, R.raw.img_xiongmaoren57,
                R.raw.img_xiongmaoren58, R.raw.img_xiongmaoren59, R.raw.img_xiongmaoren60,
                R.raw.img_xiongmaoren61, R.raw.img_xiongmaoren62, R.raw.img_xiongmaoren63,
                R.raw.img_xiongmaoren64, R.raw.img_xiongmaoren65, R.raw.img_xiongmaoren66,
                R.raw.img_xiongmaoren67, R.raw.img_xiongmaoren68, R.raw.img_xiongmaoren69,
                R.raw.img_xiongmaoren70, R.raw.img_xiongmaoren71, R.raw.img_xiongmaoren72,
                R.raw.img_xiongmaoren73, R.raw.img_xiongmaoren74, R.raw.img_xiongmaoren75,
                R.raw.img_xiongmaoren76, R.raw.img_xiongmaoren77, R.raw.img_xiongmaoren78,
                R.raw.img_xiongmaoren79, R.raw.img_xiongmaoren80, R.raw.img_xiongmaoren81)
        val HUA_JI_LIST = intArrayOf(
                R.raw.img_huaji1, R.raw.img_huaji2, R.raw.img_huaji3,
                R.raw.img_huaji4, R.raw.img_huaji5, R.raw.img_huaji6,
                R.raw.img_huaji7, R.raw.img_huaji8, R.raw.img_huaji9,
                R.raw.img_huaji10, R.raw.img_huaji11, R.raw.img_huaji12,
                R.raw.img_huaji13, R.raw.img_huaji14, R.raw.img_huaji15,
                R.raw.img_huaji16, R.raw.img_huaji17, R.raw.img_huaji18,
                R.raw.img_huaji19, R.raw.img_huaji20, R.raw.img_huaji21,
                R.raw.img_huaji22, R.raw.img_huaji23, R.raw.img_huaji24,
                R.raw.img_huaji25, R.raw.img_huaji26, R.raw.img_huaji27)
    }

}