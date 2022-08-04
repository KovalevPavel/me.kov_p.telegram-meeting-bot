package me.kov_p.telegram_meeting_bot.bot.update_delegates

import me.kov_p.telegram_meeting_bot.bot.UpdateVo

interface UpdateEventDelegate {
    fun isDelegateValid(updateVo: UpdateVo): Boolean
    fun handleUpdate(updateVo: UpdateVo)
}
