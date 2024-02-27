package org.hse.moodactivities.interfaces

import androidx.fragment.app.Fragment

interface Communicator {
    fun replaceFragment(fragment: Fragment)

    fun passData(data: Data, dataType: DataType)
}
