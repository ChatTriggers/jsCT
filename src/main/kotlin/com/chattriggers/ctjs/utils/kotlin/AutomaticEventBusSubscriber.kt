package com.chattriggers.ctjs.utils.kotlin

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.LoaderException
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.discovery.ASMDataTable
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObjectInstance

object AutomaticEventSubscriber {
    private val LOGGER = LogManager.getLogger(AutomaticEventSubscriber::class.java)

    private val registered = mutableSetOf<Any>()

    fun subscribeAutomatic(mod: ModContainer, asm: ASMDataTable) {
        val modAnnotations = asm.getAnnotationsFor(mod) ?: return

        val listeners = modAnnotations.get(KotlinListener::class.java.name)

        val loader = Loader.instance().modClassLoader

        for (listener in listeners) {
            try {
                val subscriberClass = Class.forName(listener.className, false, loader) ?: continue
                val kotlinClass = subscriberClass.kotlin
                val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: continue

                if (hasObjectEventHandlers(objectInstance) && objectInstance !in registered) {
                    MinecraftForge.EVENT_BUS.register(objectInstance)
                    registered += objectInstance
                    LOGGER.debug("Registered @EventBusSubscriber object instance {}", listener.className)
                }

            } catch (e: Throwable) {
                LOGGER.error("An error occurred trying to load an @EventBusSubscriber object {} for modid {}", mod.modId, e)
                throw LoaderException(e)
            }
        }
    }

    private fun hasObjectEventHandlers(objectInstance: Any): Boolean {
        return objectInstance.javaClass.methods.any {
            !Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(SubscribeEvent::class.java)
        }
    }
}