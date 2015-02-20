<?php
// PHP VoiceBox 0.6
// Forked by TiBounise (http://tibounise.com) based on the inital code of mGeek (http://mgeek.fr)

class Voxygen {
    /**
     * Status of the grommo filter
     * 
     * @var boolean
     * @access private
     */
    private $grommo;

    /**
     * Path to the cache folder
     * 
     * @var string
     * @access private
     */
    private $cacheFolder;

    /**
     * Array of the voices
     * 
     * @var array
     * @access public
     */
    public $voices = array();

    /**
     * Class initialisator
     * 
     * @param boolean $grommo State of the grommo filter
     * @param string $cacheFolder Path to the cache folder
     * @access public
     */
    public function __construct($grommo = false,$cacheFolder = 'cache') {
        if (is_bool($grommo)) {
            $this->grommo = $grommo;
        }
        if (is_string($cacheFolder)) {
            $this->cacheFolder = $cacheFolder;
        }
		$this->getVoices();
    }

    /**
     * Main function to do voice synthesis requests
     * 
     * @param string $voice Voice
     * @param string $text Text to be said
     * @access public
     * @return string Path to the rendered file
     */
    public function voiceSynthesis($voice,$text) {
        if (!isset( $this->voices[$voice])) {
            throw new Exception('This voice you\'ve selected is currently not implemented.');
        }

        if (get_magic_quotes_gpc()) $text = stripslashes($text);

        if ($this->grommo) {
            $text = $this->grommoFilter($text);
        }
        if (!is_dir($this->cacheFolder)) {
            mkdir($this->cacheFolder);
        }
        $md5 = md5($voice.$text);
        $file = $this->cacheFolder.'/'.$md5.'.mp3';
        if (!file_exists($file)) {
            $post = 'method=redirect&voice='.$voice.'&text='.urlencode($text).'&ts='.time();
            $voxygenResult = $this->curlJob($post);
            if ($voxygenResult !== null) {
				if( !file_put_contents($file,$voxygenResult)) {
					throw new Exception( 'Can\'t create cache file \''.$file.'\'');
				}
            } else {
                throw new Exception('Voxygen has probably changed its APIs. We can\'t get a correct URL.');
            }
        }
        return $file;   
    }

    /**
     * Grommo filter function
     * 
     * @param string $text Text to filter
     * @return string Text filtered
     * @access public
     */
    public function grommoFilter($text) {
        $text = ' '.$text.' ';
        $grommoDB = array(
            'bite'    => 'bit',
            'cul'     => 'ku',
            'putain'  => 'puh tin',
            'shit'    => 'shi ihte',
            'enculer' => 'an qu\'hulé',
            'enculé'  => 'an qu\'hulé',
            'salope'  => 'sale ôpe',
            'morsay'  => 'morsaille',
            'suce'    => 'suh sse',
            'sucer'   => 'suh ceh',
            'nems'    => 'naimes');
        foreach ($grommoDB as $normal => $equivalent) {
            $text = str_ireplace(' '.$normal.' ',' '.$equivalent.' ', $text);
            $text = str_ireplace(' '.$normal.'.',' '.$equivalent.'.', $text);
            $text = str_ireplace('.'.$normal.' ','.'.$equivalent.' ', $text);
        }
        return $text;
    }

    /**
     * HTTP request function
     * 
     * @param string $post Content of the request
     * @return string Output of the request
     * @access private
     */
    private function curlJob($post) {
        $curlHandler = curl_init("http://voxygen.fr/sites/all/modules/voxygen_voices/assets/proxy/index.php");
        curl_setopt($curlHandler, CURLOPT_HEADER, false);
        curl_setopt($curlHandler, CURLOPT_POST, true);
        curl_setopt($curlHandler, CURLOPT_POSTFIELDS, $post);
        curl_setopt($curlHandler, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curlHandler, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($curlHandler, CURLOPT_REFERER, 'http://voxygen.fr/fr');
        curl_setopt($curlHandler, CURLOPT_USERAGENT, 'iTunes/9.0.3 (Macintosh; U; Intel Mac OS X 10_6_2; en-ca)');
        curl_setopt($curlHandler, CURLOPT_COOKIE, 'has_js=1');
		curl_setopt($curlHandler, CURLOPT_FOLLOWLOCATION, true);
		#curl_setopt($curlHandler, CURLOPT_VERBOSE, true);
        curl_setopt($curlHandler, CURLOPT_HTTPHEADER, array(
            'Content-type: application/x-www-form-urlencoded',
            'X-Requested-With:  XMLHttpRequest',
            'Host: voxygen.fr'
        ));
        $output = curl_exec($curlHandler);
        curl_close($curlHandler);
        return $output;
    }

	private function getVoices() {

		$curlHandler = curl_init("http://voxygen.fr/voices.json");
        curl_setopt($curlHandler, CURLOPT_REFERER, 'http://voxygen.fr/fr');
        curl_setopt($curlHandler, CURLOPT_USERAGENT, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0');
        curl_setopt($curlHandler, CURLOPT_RETURNTRANSFER, true);
        $output = curl_exec($curlHandler);
        curl_close($curlHandler);
		$voices = json_decode( $output, true);
		if( $voices === null) {
			throw new Exception('Voxygen voices is not a valid JSON result.');
		}
		
		if( isset( $voices['groups'])) {
			if( is_array( $voices['groups'])) {
				foreach( $voices['groups'] as $lang) {
					$langName = "undefined";
					if( isset( $lang['name'])) {
						$langName = $lang['name'];
					}
					if( isset( $lang['voices'])) {
						if( is_array( $lang['voices'])) {
							foreach( $lang['voices'] as $user) {
								if( isset( $user['name'])) {
									$username = $user['name'];
									$this->voices[$username] = $langName;
								}
							}
						}
					}
				}
			}
		}

		if( !sizeof( array_keys( $this->voices))) {
			throw new Exception( 'Can\'t get voices on Voxygen site.');
		}
		ksort( $this->voices);
	}
}
?>
