package com.merseyside.merseyLib.kotlin.reflect

import com.merseyside.merseyLib.kotlin.logger.log
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType

actual class ReflectMapper {

    actual companion object {
        actual inline fun <reified T : Any, reified R : Any> map(
            value: T,
            noinline fallback: ((value: Any, inType: KType, outType: KType) -> Any)?
        ): R {
            val wrapper = ReflectWrapper(value)
            val creator = ReflectInstanceCreator.initWith<R>()

            val inParams: List<KProperty<*>> = wrapper.properties
            val outParams: List<KParameter> = creator.getPrimaryConstructorParams()

            val mappingActions: List<() -> Any?> = outParams.mapNotNull { outParam ->
                val foundProp = inParams.find { inProp -> inProp.name == outParam.name }

                if (foundProp != null) {
                    { createMappingAction(fallback, wrapper, foundProp, outParam) }
                } else {
                    if (!outParam.type.isMarkedNullable) {
                        throw NullPointerException(
                            "Can not find value for ${outParam.name} in ${R::class}. " +
                                    "Inner model is ${T::class}"
                        )
                    } else {
                        null
                    }
                }
            }

            val inArgs: List<Any?> = mappingActions.map { action -> action() }

            return creator.createInstance(*inArgs.toTypedArray())
        }

        fun createMappingAction(
            fallback: Fallback,
            wrapper: ReflectWrapper<*>,
            inProp: KProperty<*>,
            outParam: KParameter
        ): Any? {
            val inType = inProp.returnType
            val outType = outParam.type

            val inValue = wrapper.getPropertyValue(inProp) ?: return null
            return if (inType != outType) createFallbackAction(fallback, inValue, inType, outType)
            else inValue
        }

        private fun createFallbackAction(
            fallback: Fallback,
            value: Any,
            inType: KType,
            outType: KType
        ): Any {
            if (fallback == null) throw ClassCastException("Can not cast $inType to $outType and" +
                    " fallback hasn't been provided!")

            return fallback(value, inType, outType)
        }
    }

}