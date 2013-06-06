package com.example.callanalyzer.c;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;
import in.ireff.android.a.e;
import in.ireff.android.data.provider.TariffProvider;
import in.ireff.android.data.provider.d;
import java.util.ArrayList;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;

public class a
{
  private static final String a = a.class.getSimpleName();

  private static ContentValues a(in.ireff.android.data.b.c paramc)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put(d.b.a(), paramc.a);
    localContentValues.put(d.c.a(), paramc.b);
    if (paramc.c != null)
      localContentValues.put(d.d.a(), paramc.c);
    if (paramc.d != null)
      localContentValues.put(d.e.a(), paramc.d);
    if (paramc.e != null)
      localContentValues.put(d.f.a(), paramc.e);
    localContentValues.put(d.g.a(), paramc.f);
    localContentValues.put(d.h.a(), paramc.g);
    localContentValues.put(d.j.a(), paramc.h);
    localContentValues.put(d.k.a(), paramc.i);
    localContentValues.put(d.l.a(), paramc.j);
    localContentValues.put(d.m.a(), paramc.k);
    localContentValues.put(d.n.a(), paramc.l);
    localContentValues.put(d.o.a(), Long.valueOf(paramc.m));
    return localContentValues;
  }

  public static JSONArray a(Context paramContext)
  {
    return new JSONArray(com.foxykeep.datadroid.b.a.a(Uri.parse("http://app.ireff.in:9090/IreffWeb/android").buildUpon().appendQueryParameter("action", "config").appendQueryParameter("uid", e.a(paramContext)).build().toString(), 0, null, null, true).b);
  }

  public static void a(Context paramContext, String paramString1, String paramString2)
  {
    String str1 = paramString1 + "|" + paramString2;
    ContentProviderClient localContentProviderClient = paramContext.getContentResolver().acquireContentProviderClient("in.ireff.android.provider.TariffProvider");
    TariffProvider localTariffProvider = (TariffProvider)localContentProviderClient.getLocalContentProvider();
    in.ireff.android.data.b.a locala1 = localTariffProvider.a(str1);
    in.ireff.android.data.b.a locala2;
    if (locala1 != null)
    {
      if (locala1.c + 1000 * locala1.e > System.currentTimeMillis())
      {
        Log.d(a, "Skipped loading");
        localContentProviderClient.release();
        return;
      }
    }
    else
    {
      locala2 = new in.ireff.android.data.b.a();
      locala2.b = (paramString1 + "|" + paramString2);
    }
    for (in.ireff.android.data.b.a locala3 = locala2; ; locala3 = locala1)
    {
      String str2 = Uri.parse("http://app.ireff.in:9090/IreffWeb/android").buildUpon().appendQueryParameter("circle", paramString1).appendQueryParameter("service", paramString2).appendQueryParameter("uid", e.a(paramContext)).appendQueryParameter("v", "2").build().toString();
      ArrayList localArrayList1 = new ArrayList();
      if (locala3 != null)
        localArrayList1.add(new BasicHeader("If-Modified-Since", locala3.d));
      com.foxykeep.datadroid.b.c localc = com.foxykeep.datadroid.b.a.a(str2, 0, null, localArrayList1, true);
      ArrayList localArrayList2;
      int i;
      ContentValues[] arrayOfContentValues;
      if (localc != null)
      {
        localArrayList2 = in.ireff.android.data.a.a.a(localc.b);
        ContentResolver localContentResolver = paramContext.getContentResolver();
        Uri localUri = in.ireff.android.data.provider.c.b;
        String[] arrayOfString = new String[2];
        arrayOfString[0] = paramString1;
        arrayOfString[1] = paramString2;
        localContentResolver.delete(localUri, "circle = ? AND service = ?", arrayOfString);
        i = localArrayList2.size();
        if ((localArrayList2 != null) && (i > 0))
          arrayOfContentValues = new ContentValues[i];
      }
      Header[] arrayOfHeader;
      int k;
      for (int m = 0; ; m++)
      {
        if (m >= i)
        {
          paramContext.getContentResolver().bulkInsert(in.ireff.android.data.provider.c.b, arrayOfContentValues);
          arrayOfHeader = localc.a;
          int j = arrayOfHeader.length;
          k = 0;
          if (k < j)
            break label405;
          locala3.c = System.currentTimeMillis();
          localTariffProvider.a(locala3);
          localTariffProvider.a();
          localContentProviderClient.release();
          break;
        }
        arrayOfContentValues[m] = a((in.ireff.android.data.b.c)localArrayList2.get(m));
      }
      label405: Header localHeader = arrayOfHeader[k];
      if (localHeader.getName().equals("Last-Modified"))
        locala3.d = localHeader.getValue();
      while (true)
      {
        k++;
        break;
        if ((!localHeader.getName().equals("Cache-Control")) || (!localHeader.getValue().startsWith("max-age:")))
          continue;
        locala3.e = Integer.valueOf(localHeader.getValue().replaceFirst("max-age:", "").replaceFirst(", .*", "")).intValue();
      }
    }
  }

  public static void a(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    ContentProviderClient localContentProviderClient = paramContext.getContentResolver().acquireContentProviderClient("in.ireff.android.provider.TariffProvider");
    ArrayList localArrayList = in.ireff.android.data.a.a.a(com.foxykeep.datadroid.b.a.a(Uri.parse("http://app.ireff.in:9090/IreffWeb/android").buildUpon().appendQueryParameter("circle", paramString1).appendQueryParameter("service", paramString2).appendQueryParameter("query", paramString3).appendQueryParameter("uid", e.a(paramContext)).build().toString(), 0, null, null, true).b);
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Uri localUri = in.ireff.android.data.provider.c.b;
    String[] arrayOfString = new String[2];
    arrayOfString[0] = paramString1;
    arrayOfString[1] = paramString2;
    localContentResolver.delete(localUri, "circle = ? AND service = ? AND LENGTH(query) > 0", arrayOfString);
    int i = localArrayList.size();
    ContentValues[] arrayOfContentValues;
    if ((localArrayList != null) && (i > 0))
      arrayOfContentValues = new ContentValues[i];
    for (int j = 0; ; j++)
    {
      if (j >= i)
      {
        paramContext.getContentResolver().bulkInsert(in.ireff.android.data.provider.c.b, arrayOfContentValues);
        localContentProviderClient.release();
        return;
      }
      arrayOfContentValues[j] = a((in.ireff.android.data.b.c)localArrayList.get(j));
    }
  }
}

/* Location:           E:\MobileDevelopment\Utils\tools\classes-dex2jar.jar
 * Qualified Name:     in.ireff.android.data.c.a
 * JD-Core Version:    0.6.0
 */
