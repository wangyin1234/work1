package com.wison.purchase.packages.comm.utils;

import cn.hutool.core.lang.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtils {

    private static final Map<String, JAXBContext> JAXB_CONTEXT_MAP = new HashMap<>();

    public static <T> String toXml(T t) {
        if (t == null) {
            log.warn("[XmlUtils toXml] params is null");
            return null;
        }

        JAXBContext jaxbContext = getJAXBContext(t.getClass());
        Marshaller jaxbMarshaller;
        try (StringWriter stringWriter = new StringWriter()) {
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(t, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException | IOException e) {
            log.error("[XmlUtils toXml] exception", e);
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T xmlToBean(String xml, TypeReference<T> reference) {
        JAXBContext jaxbContext = getJAXBContext(reference.getClass());
        try (StringReader stringReader = new StringReader(xml)) {
            Unmarshaller un = jaxbContext.createUnmarshaller();
            return (T) un.unmarshal(stringReader);
        } catch (JAXBException e) {
            log.error("[XmlUtils toXml] exception", e);
        }
        return null;
    }

    /**
     * 根据类名称获取 JAXBContext
     */
    private static <T> JAXBContext getJAXBContext(Class<T> clazz) {
        JAXBContext jaxbContext = JAXB_CONTEXT_MAP.get(clazz.getName());
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz);
                JAXB_CONTEXT_MAP.put(clazz.getName(), jaxbContext);
            } catch (JAXBException e) {
                log.error(e.getMessage(), e);
            }
        }
        return jaxbContext;
    }
}
