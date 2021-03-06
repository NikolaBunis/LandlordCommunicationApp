package com.app.landlordcommunication.repositories.residence;

import com.app.landlordcommunication.http.HttpRequester;
import com.app.landlordcommunication.models.Residence;
import com.app.landlordcommunication.parsers.base.JsonParser;
import com.app.landlordcommunication.repositories.residence.base.ResidenceRepository;

import java.io.IOException;
import java.util.List;

public class HttpResidenceRepository implements ResidenceRepository {

    private final HttpRequester mHttpRequester;
    private final String mServerUrl;
    private final JsonParser<Residence> mJsonParser;

    public HttpResidenceRepository(HttpRequester httpRequester, String serverUrl, JsonParser<Residence> jsonParser) {
        mHttpRequester = httpRequester;
        mServerUrl = serverUrl;
        mJsonParser = jsonParser;
    }

    @Override
    public List<Residence> getResidencesByUser(int userId) throws IOException {
        String url = mServerUrl + "/" + userId;
        String json = mHttpRequester.get(url);
        return mJsonParser.fromJsonArray(json);
    }

    @Override
    public Residence changeResidenceDates(int residenceId) throws IOException {
        String url = mServerUrl + "/update/" + residenceId;
        String json = mHttpRequester.get(url);
        return mJsonParser.fromJson(json);
    }

    @Override
    public Residence getResidenceById(int residenceId) throws IOException {
        String url = mServerUrl + "/byID/" + residenceId;
        String json = mHttpRequester.get(url);
        return mJsonParser.fromJson(json);
    }
}
