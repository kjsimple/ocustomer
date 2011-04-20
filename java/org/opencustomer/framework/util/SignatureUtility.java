/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurbüro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.framework.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * Utility for generating and testing signatures using MD-5. This utility can be
 * used for simple text string and for whole files, too.
 * 
 * @author Thomas Bader (thomas.bader@bader-jene.de)
 * @version 1.0
 */
public final class SignatureUtility
{
    /**
     * Used internal logger for this class.
     */
    private static final Logger log = Logger.getLogger(SignatureUtility.class);

    /**
     * The size of the buffer array to stream the signed source.
     */
    private final static int BUFFER_ARRAY_SIZE = 4096;

    /**
     * The used algorithm of this signature utility instance.
     */
    private String algorithm = null;

    /**
     * The singleton instance of this signature utility
     */
    private static SignatureUtility instance = null;

    public static SignatureUtility getInstance()
    {
        return getInstance("MD5");
    }

    private static SignatureUtility getInstance(String algorithm)
    {
        if (instance == null)
            instance = new SignatureUtility(algorithm);

        return instance;
    }

    /**
     * Private Constructor - instances are not necessary for this class.
     */
    private SignatureUtility(String algorithm)
    {
        this.algorithm = algorithm;
    }

    /**
     * Creates a unique signature for the given inputstream.
     * 
     * @param inputStream the inputstream be be signed.
     * @return the signature of the inputstream.
     */
    private String createSignature(InputStream inputStream) throws IOException
    {
        String result = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            DigestInputStream dis = new DigestInputStream(inputStream, md);

            byte[] buffer = new byte[BUFFER_ARRAY_SIZE];
            int size = 0;
            while ((size = dis.read(buffer)) != -1)
                // streams the content (nothing has to be done)
                ;

            byte[] signature = md.digest();

            dis.close();

            result = bytesToHexString(signature);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("unknown algorithm", e);
        }

        return result;
    }

    /**
     * Creates a unique signature for the given file.
     * 
     * @param file the file be be signed.
     * @return the signature of file.
     */
    public String createSignature(File file) throws FileNotFoundException, IOException
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);

            return createSignature(fis);
        }
        finally
        {
            if (fis != null)
                fis.close();
        }
    }

    /**
     * Creates a unique signature for the given array of bytes.
     * 
     * @param bytes the array of bytes be be signed.
     * @return the signature of the array of bytes.
     */
    private String createSignature(byte[] bytes)
    {
        ByteArrayInputStream bis = null;

        try
        {
            bis = new ByteArrayInputStream(bytes);

            return createSignature(bis);
        }
        catch (IOException e)
        {
            log.error("could not read from stream", e);
        }
        finally
        {
            if (bis != null)
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    log.error("could not close stream", e);
                }
        }

        return null;
    }

    /**
     * Creates a unique signature for the given string.
     * 
     * @param text the string be be signed.
     * @return the signature of the string.
     */
    public String createSignature(String text)
    {
        return createSignature(text.getBytes());
    }

    /**
     * Checks if the signature of a text equals to an existing signature.
     * 
     * @param signature the existing signature.
     * @param text the text to be checked against the signature.
     * @return true, if the text has a signature equals to the existing
     *         signature, otherwise false.
     */
    public boolean isSignatureValid(String signature, String text)
    {
        return isSignatureValid(signature, text.getBytes());
    }

    /**
     * Checks if the signature of an array of bytes equals to an existing
     * signature.
     * 
     * @param signature the existing signature.
     * @param bytes the array of bytes to be checked against the signature.
     * @return true, if the array of bytes has a signature equals to the
     *         existing signature, otherwise false.
     */
    private boolean isSignatureValid(String signature, byte[] bytes)
    {
        return signature.equals(createSignature(bytes));
    }

    /**
     * Checks if the signature of a file equals to an existing signature.
     * 
     * @param signature the existing signature.
     * @param file the file to be checked against the signature.
     * @return true, if the array of bytes has a signature equals to the
     *         existing signature, otherwise false.
     * @throws IOException if the data could not be read from the file.
     * @throws FileNotFoundException if the file could not be found.
     */
    public boolean isSignatureValid(String signature, File file) throws FileNotFoundException, IOException
    {
        return signature.equals(createSignature(file));
    }

    /**
     * Transforms the array of bytes to a String of hexadecimal values. The
     * String has a fixed length of 2 chars per byte. This method works as
     * beautifier for the output.
     * 
     * @param bytes the array of bytes to be transformed.
     * @return a string of hexadecimal values.
     */
    private String bytesToHexString(byte[] bytes)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(new Byte(bytes[i]).intValue() - Byte.MIN_VALUE);
            if (hex.length() == 1)
                buf.append("0");
            buf.append(hex);
        }

        return buf.toString().toUpperCase();
    }

}