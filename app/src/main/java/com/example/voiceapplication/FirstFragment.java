package com.example.voiceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voiceapplication.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SpeechRecognizer mRecorder;
    View root;
    private Button startVoiceButton;
    private Button stopVoiceButton;
    private TextView voiceText;

    private RecognitionListener mRecognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            stopRecording();
        }

        @Override
        public void onError(int error) {
            stopRecording();
        }

        @Override
        public void onResults(Bundle results) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            String voice = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
            if (voice.length() > 0){
                voiceText.setText(voice);
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        root = inflater.inflate(R.layout.fragment_first, container,false);
        voiceText = root.findViewById(R.id.textview_first);
        startVoiceButton = root.findViewById(R.id.button_voice_start);
        startVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecorder = SpeechRecognizer.createSpeechRecognizer(getContext());
                mRecorder.setRecognitionListener(mRecognitionListener);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//                try {
//                    startActivityForResult(intent, 100);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                mRecorder.startListening(intent);
            }
        });

        stopVoiceButton = root.findViewById(R.id.button_voice_stop);
        stopVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        voiceText.setText(data.getStringArrayExtra(RecognizerIntent.EXTRA_RESULTS)[0]);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void stopRecording(){
        if (mRecorder != null){
            mRecorder.stopListening();
            mRecorder.cancel();
            mRecorder.destroy();
            mRecorder = null;
        }
    }

}