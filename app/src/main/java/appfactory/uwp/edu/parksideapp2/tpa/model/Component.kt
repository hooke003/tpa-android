package appfactory.uwp.edu.parksideapp2.tpa.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Component (@StringRes val name: Int,
                      @DrawableRes val lgIcon: Int,
                      @DrawableRes val smIcon: Int,
                      @ColorRes val color: Int,
                      val subscribable: Boolean,
                      var subscribed: Boolean?,
                      val topic: Topic?
)
