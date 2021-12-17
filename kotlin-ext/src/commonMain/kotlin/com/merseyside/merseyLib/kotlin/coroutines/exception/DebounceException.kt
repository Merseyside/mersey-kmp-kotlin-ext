package com.merseyside.merseyLib.kotlin.coroutines.exception

import kotlinx.coroutines.CancellationException

class DebounceException(msg: String) : CancellationException(msg)